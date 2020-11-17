//加载应该所有的科室，供选择
layui.use(['layer','jquery','table','laytpl','form'], function(){
    var $ = layui.$,
        layer = layui.layer,
    	table = layui.table,
        form = layui.form,
        laytpl = layui.laytpl;

	var setting = {
		/*view: {
			addHoverDom: addHoverDom,
			removeHoverDom: removeHoverDom,
			selectedMulti: false
		},*/
		/*check: {
			enable: true
		},*/
		data: {
			simpleData: {
				enable: true,
                rootPId: 0
			}
		},
		/*edit: {
			enable: true,
			editNameSelectAll: true,
			removeTitle: '删除部门',
			renameTitle: '编辑部门'
		},*/
		callback: {
			//  beforeRename : beforeRename, //编辑结束时触发，用来验证输入的数据是否符合要求(也是根据返回值 true|false 确定是否可以编辑完成)	
			// beforeRemove: beforeRemove, //点击删除时触发，用来提示用户是否确定删除（可以根据返回值 true|false 确定是否可以删除
			// beforeEditName: beforeEditName,
            // 单击事件
            onClick: zTreeOnClick
		}
	};	

	
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
    	$('#orgId').val(treeNode.id);
        $('#orgName').val(treeNode.name);
        $('#orgId2').val(treeNode.id);
        $('#orgName2').val(treeNode.name);
        $.ajax({
            url: 'getItemsByOrgId',
            type: 'POST',
            data: "orgId="+treeNode.id,
            dataType: 'json',
            success: function(data) {
                if(data.success) {

                    $("#itemId").empty();
                    $("#itemId2").empty();
                    var optionEle = $("<option></option>").append("").attr("value","");
                    optionEle.appendTo($('#itemId'));
                    var optionEle = $("<option></option>").append("").attr("value","");
                    optionEle.appendTo($('#itemId2'));
                    form.render('select');

					var list = data.data;
					var itemsThatBoundToTheOrgById = list[0];
					var itemsThatNotBoundToTheOrg = list[1];
                    $.each(itemsThatNotBoundToTheOrg,function () {
                        var optionEle = $("<option></option>").append(this.itemName).attr("value",this.itemId);
                        optionEle.appendTo($('#itemId'));
                    });
                    $.each(itemsThatBoundToTheOrgById,function () {
                        var optionEle = $("<option></option>").append(this.itemName).attr("value",this.itemId);
                        optionEle.appendTo($('#itemId2'));
                    });
                    //重新渲染选择框
                    form.render('select');
                } else {
                    layer.msg(data.msg, {icon: 5}, 1500);
                }
            },
            error: function(data) {
                layer.msg(data.message, {icon: 5},1500);
            }
        });
    }

    form.on('submit(sub)', function(data){
        $.ajax({
            url: 'bindOrgAndItems',
            type: 'POST',
			data: $('#binding').serialize(),
            dataType: 'json',
            success: function(data) {
                if(data.success) {
                    layer.msg(data.msg, {icon: 6}, 1500);
                    form.val('binding',{
                    	"orgId":"",
                        "orgName":"",
                        "itemId":"",
                        "itemName":""
					});
                    form.val('unbound',{
                        "orgId":"",
                        "orgName":"",
                        "itemId":""
                    });
                    $("#itemId").empty();
                    $("#itemId2").empty();
                    var optionEle = $("<option></option>").append("").attr("value","");
                    optionEle.appendTo($('#itemId'));
                    var optionEle = $("<option></option>").append("").attr("value","");
                    optionEle.appendTo($('#itemId2'));
                    form.render('select');
                } else {
                    layer.msg(data.msg, {icon: 5}, 1500);
                }
            },
            error: function(data) {
                layer.msg(data.message, {icon: 5},1500);
            }
        });

        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    form.on('submit(sub2)', function(data){
        $.ajax({
            url: 'unboundOrgAndItems',
            type: 'POST',
            data: $('#unbound').serialize(),
            dataType: 'json',
            success: function(data) {
                if(data.success) {
                    layer.msg(data.msg, {icon: 6}, 1500);
                    form.val('binding',{
                        "orgId":"",
                        "orgName":"",
                        "itemId":"",
                        "itemName":""
                    });
                    form.val('unbound',{
                        "orgId":"",
                        "orgName":"",
                        "itemId":""
                    });
                    $("#itemId").empty();
                    $("#itemId2").empty();
                    var optionEle = $("<option></option>").append("").attr("value","");
                    optionEle.appendTo($('#itemId'));
                    var optionEle = $("<option></option>").append("").attr("value","");
                    optionEle.appendTo($('#itemId2'));
                    form.render('select');
                } else {
                    layer.msg(data.msg, {icon: 5}, 1500);
                }
            },
            error: function(data) {
                layer.msg(data.message, {icon: 5},1500);
            }
        });

        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    form.on('select(select)', function(data){
    	$('#itemName').val(this.innerText);
    });
});