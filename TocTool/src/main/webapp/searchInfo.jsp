<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="page" uri="page-taglib" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>根据git url地址获取发布文档相关信息</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

  	<link rel="stylesheet" href="./css/bootstrap.min.css" type="text/css"></link>
  	<script src="./js/jquery.min.js"></script>
  	<script src="./js/bootstrap.min.js"></script>
<style>
table{
	margin:15px;
}
th{
	width:20%;
}
</style>  	
<script type="text/javascript">
function search(){
	var gitUrl = $("#gitUrl").val();
    jQuery.ajax({
	    cache: true,
		type: "POST",
		url:'<c:url value="/searchInfo"/>',
		async: false,
		data: {"gitUrl":gitUrl},
		dataType:"JSON",
		success:function(json){
			var jsonResult = json.json.docInfo;
			var ifRelease = jsonResult.ifRelease;
			if(ifRelease == "已发布"){
				$("#infoTable").empty();
			 	jQuery("#infoTable").append("<tr><th>产品</th><td>"+jsonResult.product+"</td></tr>");
			    jQuery("#infoTable").append("<tr><th>文档标题</th><td>"+jsonResult.title+"</td></tr>");
			    jQuery("#infoTable").append("<tr><th>发布时间</th><td>"+jsonResult.releaseTime+"</td></tr>");
			    jQuery("#infoTable").append("<tr><th>是否已经发布</th><td>"+jsonResult.ifRelease+"</td></tr>");
			    jQuery("#infoTable").append("<tr><th>一级菜单名称</th><td>"+jsonResult.firstMenu+"</td></tr>");
			    jQuery("#infoTable").append("<tr><th>二级菜单名称</th><td>"+jsonResult.secondMenu+"</td></tr>");
			    jQuery("#infoTable").append("<tr><th>三级菜单名称</th><td>"+jsonResult.thirdMenu+"</td></tr>");
			    jQuery("#infoTable").append("<tr><th>四级菜单名称</th><td>"+jsonResult.fourthMenu+"</td></tr>");
			    jQuery("#infoTable").append("<tr><th>五级菜单名称</th><td>"+jsonResult.fiveMenu+"</td></tr>");
			    jQuery("#infoTable").append("<tr><th>官网链接</th><td>"+jsonResult.webUrl+"</td></tr>");
			    jQuery("#infoTable").append("<tr><th>GitHub链接</th><td>"+jsonResult.githubUrl+"</td></tr>");
			}else{
				$("#infoTable").empty();
				jQuery("#infoTable").append("<tr><th>产品</th><td></td></tr>");
			    jQuery("#infoTable").append("<tr><th>文档标题</th><td></td></tr>");
			    jQuery("#infoTable").append("<tr><th>发布时间</th><td></td></tr>");
			    jQuery("#infoTable").append("<tr><th>是否已经发布</th><td>"+jsonResult.ifRelease+"</td></tr>");
			    jQuery("#infoTable").append("<tr><th>一级菜单名称</th><td></td></tr>");
			    jQuery("#infoTable").append("<tr><th>二级菜单名称</th><td></td></tr>");
			    jQuery("#infoTable").append("<tr><th>三级菜单名称</th><td></td></tr>");
			    jQuery("#infoTable").append("<tr><th>四级菜单名称</th><td></td></tr>");
			    jQuery("#infoTable").append("<tr><th>五级菜单名称</th><td></td></tr>");
			    jQuery("#infoTable").append("<tr><th>官网链接</th><td></td></tr>");
			    jQuery("#infoTable").append("<tr><th>GitHub链接</th><td></td></tr>");
			    alert("未发布");
			}
		}
		 
   	}); 
}  	
</script>

  </head>
  
  <body >
	  <div id="header" style="height:35px;margin:20px;">
	  	<h4 style="float:left"> 根据git url地址获取发布文档相关信息 </h4>
	  	<button id="searchAll" onclick="window.location='index.jsp'" type="button" class="btn btn-info btn-sm" style="float:right">返回全量查询</button>
	  </div>
 	 <hr>
    <form id="gitForm" class="form-horizontal" role="form" method="post" style="margin:20px;">
		<label for="gitUrl" style="width:5%;float:left;">gitUrl:</label>
		<input id="gitUrl" type="text" class="form-control" style="width:70%;float:left;" placeholder="eg:https://github.com/tencentyun/qcloud-documents/blob/master/product/计算与网络/云服务器/产品简介/云服务器概述.md">
		<button id="searchInfo" onclick="search()" type="button" class="btn btn-primary" style="width:6%;folat:right;margin-left:20px;">Search</button>
	</form>
	
	<div id="response"  style="margin-right:30px;font-size:12px;">
    <table id="infoTable" class="table table-bordered">
    
	</table> 
	</div>
  </body>
</html>
