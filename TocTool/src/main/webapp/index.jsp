<%@ page language="java" import="java.util.*,com.framework.page.PageConstant" pageEncoding="utf-8"%>
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
    
    <title>全量信息查询</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

	<!--  Bootstrap -->
  	 <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"></link> 
  	
  	<script type="text/javascript" src="js/jquery.min.js"></script>
  	<script type="text/javascript" src="js/jquery-1.7.2.js"></script>
  	<script type="text/javascript" src="js/bootstrap.min.js"></script> 
  	
  	
  	
<script type="text/javascript">
  		
//导出中国站映射信息为Excel
function exportChMapping(){
	window.location.href="./exportAll/exportChMapping";
} 

//导出国际站映射信息为Excel
function exportIntlMapping(){
	window.location.href="./exportAll/exportIntlMapping";
}

//导出中国站git仓库全量md文件
function exportChMd(){
	window.location.href="./exportAll/exportChMd";
}

//导出国际站git仓库全量md文件
function exportIntlMd(){
	window.location.href="./exportAll/exportIntlMd";
}

// 导出中国站gitUrl映射
function exportChGitMap() {
	window.location.href="./exportAll/exportChGitMap";
}

//导出中国站gitUrl映射
function exportIntlGitMap() {
	window.location.href="./exportAll/exportIntlGitMap";
}

</script>
  	
  </head>
  
  <body>
    <form class="form-horizontal" role="form">
		<div class="form-group">
		<div style="margin: 15px;">
			
			<button id="export_ChMapping" onclick="exportChMapping()" type="button" class="btn btn-primary">导出中国站官网映射</button>
			<button id="export_IntlMapping" onclick="exportIntlMapping()" type="button" class="btn btn-primary">导出国际站官网映射</button>
			
			<button id="export_ChMd" onclick="exportChMd()" type="button" class="btn btn-primary">导出中国站git仓库md表</button>
			<button id="export_IntlMd" onclick="exportIntlMd()" type="button" class="btn btn-primary">导出国际站git仓库md表</button>
			
			<button id="export_ChGitMap" onclick="exportChGitMap()" type="button" class="btn btn-primary">导出中国站git映射</button>
			<button id="export_IntlGitMap" onclick="exportIntlGitMap()" type="button" class="btn btn-primary">导出国际站git映射</button>
			
			<button id="searchInfo" onclick="window.location='search.jsp'" type="button" class="btn btn-info btn-sm" style="margin-left:20px;margin-top:5px;">按条件查询</button>
			<button id="searchInfo" onclick="window.location='searchInfo.jsp'" type="button" class="btn btn-info btn-sm" style="float:right; margin-top:5px;">根据gitUrl单条查询</button>
			
		</div>
		</div>
	</form>
	<hr>
	
  </body>
</html>
