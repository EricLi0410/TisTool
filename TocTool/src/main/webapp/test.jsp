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
<title>Insert title here</title>
<script>
$(function(){
  var conList = $('.tbConIndex');
  $(conList).each(function () {
    var tbodyName = $(this).attr('id').slice(2);
    if ($(this).height() > 216) {
    $('#div' + tbodyName).css('paddingRight', '10px');
  } else {
    $('#div' + tbodyName).css('paddingRight', '0');
 }
    });
})
</script>

<style>
.intblayout{ table-layout: fixed;width: 100%;padding: 0;}
.nonetr{height:0px;line-height:0;margin:0;padding:0;}

</style>

</head>
<body>

<table class="indexList f_left" width="49%" border="1" style="margin-left: 1%;">
         <table class="intblayout" border="1">
             <tr>
                 <th>待办类型</th>
                 <th>待办任务名称</th>
                 <th>最晚处理日期</th>
                 <th>处理状态</th>
             </tr>
         </table>
     <div style="overflow-y: auto;height: 100px;">
         <table class="intblayout"  border="1" >
         	<tbody id="tbSysTask" class="tbConIndex">
             <tr>
                 <td>aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa</td>
                 <td>a</td>
                 <td>a</td>
                 <td>a</td>
             </tr>
             <tr>
                 <td>b</td>
                 <td>b</td>
                 <td>b</td>
                 <td>b</td>
             </tr>
             <tr>
                 <td>a</td>
                 <td>a</td>
                 <td>a</td>
                 <td>a</td>
             </tr>
             <tr>
                 <td>b</td>
                 <td>b</td>
                 <td>b</td>
                 <td>b</td>
             </tr>
             <tr>
                 <td>a</td>
                 <td>a</td>
                 <td>a</td>
                 <td>a</td>
             </tr>
             <tr>
                 <td>b</td>
                 <td>b</td>
                 <td>b</td>
                 <td>b</td>
             </tr>
             </tbody>
         </table>
     </div>
</table>
            
</body>
</html>