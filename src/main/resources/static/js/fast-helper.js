// 获取项目路径
function getContextPath(){return "/shuihuanjing/";}

function build_path(url){return getContextPath() + url;}

function alert_msg(msg){
	var icon = {time:1500}
	layer.msg(msg,icon);   
}

function alert_no(msg){
	var icon = {time:1500, icon:5}
	layer.msg(msg,icon);   
}

function alert_yes(msg){
	var icon = {time:1500, icon:6}
	layer.msg(msg,icon);   
}