//加载应该所有的科室，供选择
layui.use(['layer','jquery','table','laytpl'], function(){
    var $ = layui.$,
        layer = layui.layer,
    	table = layui.table,
        laytpl = layui.laytpl;

	var setting = {
		view: {
			addHoverDom: addHoverDom,
			removeHoverDom: removeHoverDom,
			selectedMulti: false
		},
		/*check: {
			enable: true
		},*/
		data: {
			simpleData: {
				enable: true,
                rootPId: 0
			}
		},
		edit: {
			enable: true,
			editNameSelectAll: true,
			removeTitle: '删除部门',
			renameTitle: '编辑部门'
		},
		callback: {
			//  beforeRename : beforeRename, //编辑结束时触发，用来验证输入的数据是否符合要求(也是根据返回值 true|false 确定是否可以编辑完成)	
			beforeRemove: beforeRemove, //点击删除时触发，用来提示用户是否确定删除（可以根据返回值 true|false 确定是否可以删除
			beforeEditName: beforeEditName,
            // 单击事件
            onClick: zTreeOnClick
		}
	};	
	//添加节点,交互后台
	function addHoverDom(treeId, treeNode) {
		var sObj = $("#" + treeNode.tId + "_span");
		if(treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0) return;
		var addStr = "<span class='button add' id='addBtn_" + treeNode.tId +
			"' title='添加部门' onfocus='this.blur();'></span>";
		sObj.after(addStr);
		var btn = $("#addBtn_" + treeNode.tId);
		if(btn) btn.bind("click", function() {
			layer.open({
				type: 2,
				content: 'toAddOrganizationPage',
				area: ['30%', '50%'],
				btn: ['确定', '取消'],
				yes: function(index, layero) {
					var submitID = 'LAY-dept-add-submit',
						submit = layero.find('iframe').contents().find('#' + submitID);
					submit.trigger('click');
				},
				success: function(layero, index) {
					var body = layer.getChildFrame('body', index);
					body.find("#pid").val(treeNode.id);
				}
			});
			return false;
		});
	};
	//添加节点,视觉上添加，在添加节点交互后台成功后调用
	window.addHoverDomVision = function(id, pid, name) {
		var zTree = $.fn.zTree.getZTreeObj("officemna");
		var treeNode = zTree.getNodeByParam("id", pid, null);
		zTree.addNodes(treeNode, {
			id: id,
			pId: pid,
			name: name
		});
	};

	function removeHoverDom(treeId, treeNode) {
		$("#addBtn_" + treeNode.tId).unbind().remove();
	};

	// 删除树节点
	function beforeRemove(treeId, treeNode) {
		var zTree = $.fn.zTree.getZTreeObj(treeId);
		var idList = treeNode.id;
		idList = getAllChildrenNodes(treeNode,idList);
		layer.confirm("确认删除 " + treeNode.name + " 吗？", {
			btn: ['是', '否']
		}, function(index) {
			$.ajax({
				url: 'deleteOrganizationInfoById',
				type: 'POST',
				dataType: 'json',
				data : {
					idList : idList
				},
				success: function(data) {
					if(data.success) {
						zTree.removeNode(treeNode);
						layer.close(index);
						alertMessage("删除成功");
					} else {
						layer.msg(data.msg, {
							icon: 5
						});
					}
				},
				error: function() {
					layer.msg('系统错误', {
						icon: 5
					});
				}
			});
		}, function(index) {
			layer.close(index);
		});
		return false;
	}
	
	function getAllChildrenNodes(treeNode,result){
	      if (treeNode.isParent) {
	        var childrenNodes = treeNode.children;
	        if (childrenNodes) {
	            for (var i = 0; i < childrenNodes.length; i++) {
	                result += ',' + childrenNodes[i].id;
	                result = getAllChildrenNodes(childrenNodes[i], result);
	            }
	        }
	    }
	    return result;
	}
	
	//编辑树节点
	function beforeEditName(treeId, treeNode) {
		layer.open({
			type: 2,
			content: 'toEditOrganizationPage',
			area: ['30%', '30%'],
			btn: ['确定', '取消'],
			yes: function(index, layero) {
				var submitID = 'LAY-dept-add-submit',
					submit = layero.find('iframe').contents().find('#' + submitID);
				submit.trigger('click');
			},
			success: function(layero, index) {
				//回填赋值
				var body = layer.getChildFrame('body', index);
				body.find("#deptId").val(treeNode.id);
				body.find("#deptNode").val(treeNode.name);
			}
		});
		return false;
	}
	
	//编辑节点,视觉上编辑，在节点交互后台成功后调用
	window.editNameVision = function(id, name) {
		var zTree = $.fn.zTree.getZTreeObj("officemna");
		var nodes = zTree.getNodesByParam("id", id, null);
		nodes.name = name;
		console.log(nodes);
		console.log(zTree);
		if (nodes.length>0) {
			nodes[0].name = name;
			zTree.updateNode(nodes[0]);
		}
		//zTree.updateNode(nodes);
	};

	//加载菜单树
    $.ajax({
		url: 'getOrganziationInfoList',
		type: 'POST',
		dataType: 'json',
		success: function(data) {
			if(data.success) {
				ztree = $.fn.zTree.init($("#officemna"), setting, data.data);
             	ztree.expandAll(true);
			} else {
				layer.msg('加载部门数据节点失败', {
					icon: 5
				});
			}
		},
		error: function(data) {
			layer.msg(data.message, {
				icon: 5
			});
		}
	 });

    // 单击事件，向后台发起请求
    function zTreeOnClick(event, treeId, treeNode) {
    	//将点击的节点的id传递给隐藏的输入框，以便当添加用户时传递节点id
    	$('#tId').val(treeNode.id);
        //执行重载
        tableIns.reload({
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                id: treeNode.id,
            }
        });
    }

    //第一个实例
    var tableIns = table.render({
        elem: '#orgTable'
		,id:'orgTable'
		,method:'post'
        ,url: 'getUserInfoByOrganizationId' //数据接口
        ,page: true //开启分页
		,loading: true
		,even: false//不开启隔行背景
		,response:{
        	statusCode:1
		}
        ,request: {
            pageName: 'pageNum' ,//页码的参数名称，默认：page
            limitName: 'pageSize' //每页数据量的参数名，默认：limit
        }
		,parseData: function(res){ //将原始数据解析成 table 组件所规定的数据
            return {
                "code": res.code, //解析接口状态
                "msg": res.msg, //解析提示文本
                "count": res.data.total, //解析数据长度
                "data": res.data.list //解析数据列表
            };
        }
        ,cols: [
        	[ //表头
            {field: 'id', title: 'ID', width:80, sort: true, fixed: 'left'}
            ,{field: 'account', title: '登录账号'}
            ,{field: 'trueName', title: '人员名称'}
            ,{field: 'orgNames', title: '部门名称'}
            ,{field: 'createTime', title: '创建时间'}
            ,{field: 'leaders', title: '领导的部门'}
            ,{field: 'isLeader', title: '职位',templet:function (d) {
                    if (d.isLeader==1){
                        return "领导";
                    }else {
                        return "普通员工";
                    }
                }}
            ,{title: '操作', align: 'center', fixed: 'right', toolbar: '#orgTable-manage'}
        ]
		]
        ,text: {
            none:'无人员！'
        }
    });
    $('.selectUser').on('click', function() {
        tableIns.reload({
            page: {
                curr: 1 //重新从第 1 页开始
            },
            where: {
                tName: $("#tName").val(),
            }
        });
    });
    $('.addUser').on('click', function() {
		layer.open({
			type: 2,
			content: 'toAddUserToOrgPage' //弹窗路径
			,area: ['400px', '300px']
			,btn: ['确定', '取消']
			,yes: function(index, layero){
				var iframeWindow = window['layui-layer-iframe'+ index]
					,submitID = 'LAY-user-role-submit'
					,submit = layero.find('iframe').contents().find('#'+ submitID);
				submit.trigger('click');
			}
            ,success: function(layero, index) {
				var body = layer.getChildFrame('body', index);
				body.find("#tId").val($('#tId').val());

            }
		});
    });
    table.on('tool(orgTable)', function(obj){
        var data = obj.data;
        if(obj.event=='edit' ){
            // alert("edit");
            layer.open({
                type: 2,
                content: 'toEditUserFromOrgPage' //弹窗路径
                ,area: ['400px', '300px']
                ,btn: ['确定', '取消']
                ,yes: function(index, layero){
                    var iframeWindow = window['layui-layer-iframe'+ index]
                        ,submitID = 'LAY-user-role-submit'
                        ,submit = layero.find('iframe').contents().find('#'+ submitID);
                    submit.trigger('click');
                }
                ,success: function(layero, index) {
                    var body = layer.getChildFrame('body', index);
                    body.find("#uId").val(data.id);

                }
            });
        }else if (obj.event =='leader' ){
            layer.confirm('确定要将该用户设置为当前部门的领导吗？',function () {
                var oId = $('#tId').val();
                var uId = data.id;
                $.ajax({
                    url: 'addLeaderToOrganization',
                    data: {
                        oId : oId,
                        uId : uId
                    },
                    type: 'POST',
                    dataType: 'json',
                    success: function (data) {
                        if (data.success) {
                            layer.msg(data.msg, {icon: 6}, 1500);
                            tableIns.reload({
                                page: {
                                    curr: 1 //重新从第 1 页开始
                                },
                                where: {
                                    id: oId,
                                }
                            });
                        } else {
                            layer.msg(data.msg, {icon: 5}, 1500);
                        }

                    },
                    error: function () {
                        layer.msg('系统错误！', {
                            icon: 5
                        });

                    }
                });
            });
		}else if (obj.event =='remove_leader'){
            layer.confirm('确定要将该用户从当前部门的领导中移除吗？',function () {
                var oId = $('#tId').val();
                var uId = data.id;
                $.ajax({
                    url: 'removeLeaderFromOrganization',
                    data: {
                        oId : oId,
                        uId : uId
                    },
                    type: 'POST',
                    dataType: 'json',
                    success: function (data) {
                        if (data.success) {
                            layer.msg(data.msg, {icon: 6}, 1500);
                            tableIns.reload({
                                page: {
                                    curr: 1 //重新从第 1 页开始
                                },
                                where: {
                                    id: oId,
                                }
                            });
                        } else {
                            layer.msg(data.msg, {icon: 5}, 1500);
                        }

                    },
                    error: function () {
                        layer.msg('系统错误！', {
                            icon: 5
                        });

                    }
                });
            });
		}
    });
});