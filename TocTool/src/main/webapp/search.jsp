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
    
    <title>发布信息查询</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
  	<link rel="stylesheet" href="./css/bootstrap/bootstrap-3.3.7.min.css" type="text/css"></link>
  	<link rel="stylesheet" href="./css/bootstrap/bootstrap-select.min-1.13.9.css" type="text/css"></link>
  	<script src="./js/jquery-1.11.3.min.js"></script>
  	<script src="./js/bootstrap/bootstrap-3.3.7.min.js"></script>
  	<script src="./js/bootstrap/bootstrap-select-1.13.9.min.js"></script>

<style>
#search_group{
	margin: 15px;
	height: 30px;
	font-size: 16px;
}
.conds_group{
	margin-right: 30px;
	float: left;
}
hr {
    height: 3px;
    background: #C0C0C0;
    margin-top: 55px;
    margin-bottom: 10px;
}
#response{
	padding: 0 13px;
	font-size: 10px;
}
table{
	table-layout:fixed;
	font-size: 8px;
}
.table{ 
	table-layout: fixed;
	padding: 0;
}
td{
	word-break: break-all;
}

</style>  	
<script type="text/javascript">
$(function(){
	$("#page").attr("style","display:none;");
}); 

$('.selectpicker').selectpicker({
     'selectedText':'cat',
     'noneSelectedText':'请选择',
     'deselectAllText':'全不选',
     'selectAllText': '全选',
 })

function search(num){
	var pageSize = jQuery("#pageSize").val();
	var totalPage = jQuery("#totalPage").text();
	var params = $('#params').selectpicker('val');
    var ckDisable = $("input[name='disable']");
    var disable =[];
    for(k in ckDisable ){
	     if(ckDisable[k].checked){
	    	 disable.push(ckDisable[k].value);
	     }
    }
     var ckVisible = $("input[name='visible']");
     var visible =[];
     for(m in ckVisible ){
	     if(ckVisible[m].checked){
	    	 visible.push(ckVisible[m].value);
	     }
     }
     var ckInMenu = $("input[name='inMenu']");
     var inMenu =[];
     for(k in ckInMenu ){
	     if(ckInMenu[k].checked){
	    	 inMenu.push(ckInMenu[k].value);
	     }
     }
     var ckSeurl = $("input[name='sourceUrl']");
     var seurl =[];
     for(k in ckSeurl ){
	     if(ckSeurl[k].checked){
	    	 seurl.push(ckSeurl[k].value);
	     }
     }
     var ckReurl = $("input[name='redirectUrl']");
     var reurl =[];
     for(k in ckReurl ){
	     if(ckReurl[k].checked){
	    	 reurl.push(ckReurl[k].value);
	     }
     }
     jQuery.ajax({
	    cache: true,
		type: "POST",
		url:'<c:url value="./getInfo/searchByCond"/>',
		async: false,
		data:{ "num":num, "pageSize":pageSize, "params": params, "disable": disable, "visible":visible, "inMenu":inMenu, "sourceUrl":reurl, "redirectUrl":reurl},
		dataType:"JSON",
	 	success:function(json){
			var jsonResult = json.docLs;
			if(jsonResult.length == 0 ){
				alert("暂时没有符合该要求的文档哦~");
			}
			$("#page").attr("style","display:block;");
			$("#infoTable").empty();
	 		var tr = "<table class='table table-bordered' style='margin-bottom:0;'><tr>";
			for ( var p in params) {
				th = "<th>"+params[p]+"</th>";
				tr = tr + th;
			}
			tr = tr + "</tr></table>";
			jQuery("#infoTable").append(tr); 
			var tb = "<div style='overflow-y:auto;height:430px;'><table class='table table-bordered'><tbody id='tbSysTask' class='tbConIndex'>";
			for (var i = 0; i < jsonResult.length; i++) {
				tb = tb + "<tr>";
				for ( var p in params) {
					var value = params[p];  //定义变量，获取属性名
					th = "<td>"+jsonResult[i][value]+"</td>";
					tb = tb + th;
				}
				tb = tb + "</tr>";
		    }
			tb = tb + "</tbody></table></div>";
			jQuery("#infoTable").append(tb);
			$("#page_info").empty();
			jQuery("#page_info").append("共&nbsp&nbsp<label id='totalNums'>"+json.count+
					"</label>&nbsp&nbsp条数据，当前第&nbsp&nbsp<label id='currentPage'>"+num
			 		+"</label>&nbsp&nbsp页，共&nbsp&nbsp<label id='totalPage'>"
			 		+json.totalPage+"</label>&nbsp&nbsp页");
/*			//显示分页页码
 			var curPage = parseInt(num);
			$("#p2").text(curPage-2);
	        $("#p3").text(curPage-1);
	        $("#p4").text(curPage);
	        $("#p5").text(curPage+1);
	        $("#p6").text(curPage+2);
	        $("#p7").text(json.totalPage);
	        //页码显示处理
	      //处理当前页小于等于3的特殊情况
	        if(curPage<=3){
	            $("#prePoint").hide();
	            $("#p1").hide();
	        }//当前页是4还需要hide掉第一个省略号按钮（！重要）
	        else if(curPage==4){
	            $("#prePoint").hide();
	        }
	        //当前页是1还需要hide掉第二第三个按钮
	        if(curPage==1){
	            $("#p2").hide();
	            $("#p3").hide();
	        }
	        else if(curPage==2){
	            $("#p2").hide();
	        }

	        //最末端的特殊情况处理和最前端是一样的
	        if(curPage>=json.totalPage-2){
	            $("#sufPoint").hide();
	            $("#p7").hide();
	        }
	        else if(curPage==json.totalPage-3){
	            $("#sufPoint").hide();
	        }

	        if(curPage==json.totalPage){
	            $("#p5").hide();
	            $("#p6").hide();
	        }else if(curPage==json.totalPage-1){
	            $("#p6").hide();
	        }  */
	        
		} 
   	}); 
}  	
  
function exportByCond(){
	var params = $('#params').selectpicker('val');
	var ckWebsite = $("input[name='website']");
    var website =[];
    for(k in ckWebsite ){
	     if(ckWebsite[k].checked){
	    	 website.push(ckWebsite[k].value);
	     }
    }
	var ckDisable = $("input[name='disable']");
    var disable =[];
    for(k in ckDisable ){
	     if(ckDisable[k].checked){
	    	 disable.push(ckDisable[k].value);
	     }
    }
    var ckVisible = $("input[name='visible']");
    var visible =[];
    for(m in ckVisible ){
	     if(ckVisible[m].checked){
	    	 visible.push(ckVisible[m].value);
	     }
    }
    var ckInMenu = $("input[name='inMenu']");
    var inMenu =[];
    for(k in ckInMenu ){
	     if(ckInMenu[k].checked){
	    	 inMenu.push(ckInMenu[k].value);
	     }
    }
    var ckSeurl = $("input[name='sourceUrl']");
    var seurl =[];
    for(k in ckSeurl ){
	     if(ckSeurl[k].checked){
	    	 seurl.push(ckSeurl[k].value);
	     }
    }
    var ckReurl = $("input[name='redirectUrl']");
    var reurl =[];
    for(k in ckReurl ){
	     if(ckReurl[k].checked){
	    	 reurl.push(ckReurl[k].value);
	     }
    }
	window.location.href="./getInfo/exportByCond?param="+params+"&website="+website+"&disable="+disable+
			"&visible="+visible+"&inMenu="+inMenu+"&sourceUrl="+reurl+"&redirectUrl="+reurl;
}


//上一页
function prev(){
	var currentPage = jQuery("#currentPage").text();
	var totalPage = jQuery("#totalPage").text();
	if(currentPage=="1"){
		alert("当前已是第一页");
	}else if(currentPage>1){
		currentPage =parseInt(currentPage)-1;
		jQuery("#currentPage").text(currentPage);
		search(currentPage);
	}
}
//下一页
function next(){
	var currentPage = jQuery("#currentPage").text(); 
	var totalPage = jQuery("#totalPage").text();
	
	if(currentPage == totalPage){
		alert("当前已是最后一页");
	}else if(parseInt(currentPage) < parseInt(totalPage)){
			currentPage =parseInt(currentPage)+1;
			jQuery("#currentPage").text(currentPage); //将当前值赋值给页面上的当前页的控件
			search(currentPage);
	}
}
//首页
function first(){
	var currentPage = jQuery("#currentPage").text(); 
	var totalPage = jQuery("#totalPage").text();
	if(currentPage == '1'){
		alert("当前已是第一页");
	}else {
		currentPage = '1';
		jQuery("#currentPage").text(currentPage); //将当前值赋值给页面上的当前页的控件
		search(currentPage);
	}
}
//尾页
function last(){
	var currentPage = jQuery("#currentPage").text(); 
	var totalPage = jQuery("#totalPage").text();
	
	if(currentPage == totalPage){
		alert("当前已是最后一页");
	}else{
		currentPage = totalPage;
		jQuery("#currentPage").text(currentPage); //将当前值赋值给页面上的当前页的控件
		search(currentPage);
	}
}
// 跳转指定页
function sel_num(){
	var num = $("#sel_num").val();
	var totalPage = jQuery("#totalPage").text();//这个是你查询的总页数
	if(totalPage<num){
		alert("超范围");
	}
	else{
		search(num);
	}
}
/* 
function jump_num(obj){
	var num = $(obj).text();
	var totalPage = jQuery("#totalPage").text();
	if(totalPage<num){
		alert("超范围");
	}
	else{
		search(num);
	}
}
 */
</script>

  </head>
  
  <body>
    <div id="search_group"> 
      <div style="float:left;">                       
		<div id="param_group" class="form-group" style="float:left;">
	        <label class="control-label">展示参数：</label>
	       	<select id="params" name="params" class="selectpicker show-tick" multiple title="请选择参数" data-actions-box="true" data-width="800px">
	            <option name="产品" value="product">产品</option>
	     		<option name="产品标题" value="title">文档标题</option>
	     		<option name="语言" value="lang">语言</option>
	     		<option name="是否发布" value="ifRelease">是否发布</option>
	     		<option name="是否发布" value="releaseTime">发布时间</option>
	     		<option name="一级菜单" value="firstMenu">一级菜单</option>
	     		<option name="二级菜单" value="secondMenu">二级菜单</option>
	     		<option name="三级菜单" value="thirdMenu">三级菜单</option>
	     		<option name="四级菜单" value="fourthMenu">四级菜单</option>
	     		<option name="五级菜单" value="fiveMenu">五级菜单</option>
	     		<option name="官网链接" value="webUrl">官网链接</option>
	     		<option name="git链接" value="githubUrl">git链接</option>
	     		<option name="来源" value="source">来源</option>
	       </select>
	    </div>
	    
	    <div class="btn-group" data-toggle="buttons" style="margin-left:20px;">
  			<label class="btn btn-info active">
    		<input type="radio" name="website" value="china" autocomplete="off" checked> 中国站
  			</label>
  			<label class="btn btn-info">
   			<input type="radio" name="website" value="intl" autocomplete="off"> 国际站
  			</label>
		</div>

	    
	    <button id="searchInfo" onclick="search(1)" type="button" class="btn btn-primary" style="width:6%;folat:right;margin-left:50px;">Search</button>
	    <button id="exportInfo" onclick="exportByCond()" type="button" class="btn btn-primary" style="width:6%;folat:right;margin-left:10px;">导出excel</button>
	    
	    <div style="float:left;"> 
		    <label class="control-label" style="float:left;">查询条件：</label>
		    <div class="conds_group">
		         文档是否弃用：
			   <label class="checkbox-inline">
			      <input type="checkbox" name="disable" value="1"> 是
			   </label>
			   <label class="checkbox-inline">
			      <input type="checkbox" name="disable" value="0"> 否
			   </label>
			</div>
			<div class="conds_group">
		     链接是否可用：
			   <label class="checkbox-inline">
			      <input type="checkbox" name="visible" value="1"> 是
			   </label>
			   <label class="checkbox-inline">
			      <input type="checkbox" name="visible" value="0"> 否
			   </label>
			</div>
			<div class="conds_group">
		        文档是否在目录：
			   <label class="checkbox-inline">
			      <input type="checkbox" name="inMenu" value="1"> 是
			   </label>
			   <label class="checkbox-inline">
			      <input type="checkbox" name="inMenu" value="0"> 否
			   </label>
			</div>
			<div class="conds_group">
		     mdUrl是否为空：
			   <label class="checkbox-inline">
			      <input type="checkbox" name="sourceUrl" value="1"> 是
			   </label>
			   <label class="checkbox-inline">
			      <input type="checkbox" name="sourceUrl" value="0"> 否
			   </label>
			</div>
			<div class="conds_group">
		     重定向url是否为空：
			   <label class="checkbox-inline">
			      <input type="checkbox" name="redirectUrl" value="1"> 是
			   </label>
			   <label class="checkbox-inline">
			      <input type="checkbox" name="redirectUrl" value="0"> 否
			   </label>
			</div>
		 </div>
	   </div>
<!-- 	   
		<div id="cond_group" class="form-group" style="float:left;margin-right:50px;">
	     	<label class="control-label">查询条件：</label>
	     	<select id="conds" name="conds" class="selectpicker show-tick" multiple title="请选择查询条件" data-actions-box="true" data-width="300px">
	     		<optgroup label="文档是否弃用">
	     			<option value="0">是否弃用[否]</option>
	     			<option value="1">是否弃用[是]</option>
	     		</optgroup>
	     		<optgroup label="链接是否可访问">
	     			<option value="0">是否可访问[否]</option>
	     			<option value="1">是否可访问[是]</option>
	     		</optgroup>
	     		<optgroup label="文档是否在目录">
	     			<option value="0">是否在目录[否]</option>
	     			<option value="1">是否在目录[是]</option>
	     		</optgroup>
	     	</select>
		</div>
	 -->	
	</div> 
	<hr>
	
 	<div id="response">
    	<table id="infoTable" class="table table-bordered scrolltable">
    	 
		</table> 
	</div>
	
	<div id="page">
		<p id="page_info" class="pagination pagination-sm" style="margin-left:15px; margin-top:3px; float:left;"></p>
		<ul id="pageList" class="pagination pagination-sm"  style="margin-left:15px; margin-top:0px; flast:left;">
			<li class='page-item'><a class='page-link' onclick='first()'>首页</a></li>
			<li class='page-item'><a class='page-link' onclick='prev()'>&laquo;上一页</a></li>
	 <!-- 		
			<li class='page-item'><a id="p1" class='page-link'>1</a></li>
			<li class='page-item'><span id="prePoint">...</span></li>
			<li class='page-item'><a id="p2" class="jumpPage" onclick="jump_num(this)"></a></li>
			<li class='page-item'><a id="p3" class="jumpPage" onclick="jump_num(this)"></a></li>
			<li class='page-item'><a id="p4" class="jumpPage" onclick="jump_num(this)"></a></li>
			<li class='page-item'><a id="p5" class="jumpPage" onclick="jump_num(this)"></a></li>
			<li class='page-item'><a id="p6" class="jumpPage" onclick="jump_num(this)"></a></li>
			<li class='page-item'><span id="sufPoint">...</span></li>
			<li class='page-item'><a id="p7" class="jumpPage" onclick="jump_num(this)"></a></li>
		-->	
			<li class='page-item'><a class='page-link' onclick='next()'>下一页&raquo;</a></li>
			<li class='page-item'><a class='page-link' onclick='last()'>尾页</a></li>
			<li>&nbsp;&nbsp;&nbsp;每页显示<label>
			 <select id="pageSize" onchange="search(1)" size="1" style="padding:2px 0px; margin-top:3px;">
			  <option selected="selected" value="10">10条/页</option>
			  <option value="20">20条/页</option>
			  <option value="30">30条/页</option>
			  <option value="50">50条/页</option>
			  <option value="100">100条/页</option>
			 </select>
			 </label>
			 </li> 
			<li>&nbsp;&nbsp;&nbsp;跳转至&nbsp;<input class="sel_num" type="number" min="1" style="width:30px">&nbsp;页&nbsp;<input type="button" onclick="sel_num()" value="GO"></input></li>
		</ul>
	</div>
	
	
  </body>
</html>
