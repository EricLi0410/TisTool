
//document.write("<script language='javascript' src='resources/js/publicBasicJS/basicFunction.js'></script>");//引入获取数据的js
//document.write("<script language='javascript' src='../resources/js/publicBasicJS/basicFunction.js'></script>");


var Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 ];    // 加权因子   //身份证号码验证
var ValideCode = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ];            // 身份证验证位值.10代表X   

//var isimp=0;

var becbillNo = "";//从页面传来的清单号 或清单号
var billNo = "";//提运单号
var jumpAddress = "";
var urlpath = "";

/**
 * 控制input框只能输入数字
 * @param e
 */
function IsNum(e) {
    var k = window.event ? e.keyCode : e.which;
    if (((k >= 48) && (k <= 57)) || k == 8 || k == 0) {
    } else {
        if (window.event) {
            window.event.returnValue = false;
        }
        else {
            e.preventDefault(); //for firefox 
        }
    }
} 

/**
 * 身份证验证
 * @param idCard
 * @returns
 */
function IdCardValidate(idCard) { 
	idCard=trim(idCard.replace(/ /g, ""));               //去掉字符串头尾空格                     
	if (idCard.length == 15) {   
		return isValidityBrithBy15IdCard(idCard);       //进行15位身份证的验证    
	} else if (idCard.length == 18) {   
		var a_idCard = idCard.split("");                // 得到身份证数组   
		if(isValidityBrithBy18IdCard(idCard)&&isTrueValidateCodeBy18IdCard(a_idCard)){   //进行18位身份证的基本验证和第18位的验证
			return true;   
		}else {   
			return false;   
		}   
	} else {   
		return false;   
	}   
}   
   



/**
 * @功能 去掉字符串头尾空格
 * @param str
 * @returns
 */
function trim(str) {   
	return str.replace(/(^\s*)|(\s*$)/g, "");   
}


/**
 * 
 * @功能 校验输入浮点型数据是否符合规则
 * @param field 输入框的ID
 * @param name	名称
 * @param intLength 正式部分长度
 * @param floatLength 设置小数点后几位
 * @param minlength 设置最小长度
 * @returns
 */
function checkFloat(field, name, intLength,floatLength, minlength)
{
	var sMsg=checkString(field, name, intLength+floatLength+1, minlength);
	if(sMsg!=""){
		return sMsg;
	}
	var str = field.value;
	if(str==""){
		return "";
	}
	if(!isFloat(str)){
		sMsg+=name+"\t必须是数字，请您重新填写！\n\n";
		field.focus();
		return sMsg;
	}

	var pos = str.indexOf(".");
	if(pos!=-1){
		if(str.length-pos>floatLength+1){
			sMsg += name+"\t小数部分不能超过"+floatLength+"位，请调整\n\n";
			return sMsg;
		}
		if(pos > intLength){
			sMsg += name+"\t整数部分不能超过"+intLength+"位，请调整\n\n";
			return sMsg;
		}
	}else{
		if(str.length > intLength){
			sMsg += name+"\t整数部分不能超过"+intLength+"位，请调整\n\n";
			return sMsg;
		}
	}
	return sMsg;
}

/**
 * 
 * @功能 用于判断是否是浮点型数据 是 return TRUE 不是 return FALSE
 * @param s
 * @returns {Boolean}
 */
function isFloat(s){
	if (s.match("^(-?\\d+)(\\.\\d+)?$")){
		return true;
	}
	else{
		return false;
	}
}



/**
 * @功能 用于判断字符串是否为空
 * @param field 输入数据框的ID
 * @param name 数据名称
 * @param maxlength 最大长度
 * @param minlength 最小长度
 * @returns {String} 返回提示信息
 */
//普通字符域的判断
function checkString(field, name, maxlength, minlength)
{
	var sMsg="";
	if(minlength != 0 && field.value == "")
	{
		sMsg=name+"\t不能为空，请您填写"+name+"！\n";
		field.focus();
		return sMsg;
	}
	if(minlength != 0 && ansiLength(field.val()) < minlength)
	{
		if(minlength == maxlength)
			sMsg+=name+"\t的长度必须是"+minlength+"个字节，请您重新填写！\n";
		else
			sMsg+=name+"\t的长度至少需要"+minlength+"个字节以上，请您重新填写！\n";
		field.focus();
		return sMsg;
	}
	if(maxlength != 0 && ansiLength(field.val()) > maxlength)
	{
		sMsg+=name+"\t的长度不能超过"+maxlength+"个字节，请您重新填写！\n";
		field.focus();
		return sMsg;
	}
	return sMsg;
}

/**
 * @功能 判断输入数据是否是整数
 * @param s
 * @returns {Boolean} 是返回TRUE 否返回FALSE
 */
function isInteger(s){
	if (s.match("^\\d+$")){
		return true;
	}
	else{
		return false;
	}
}

/**
 * @功能 判断输入的字符串是否为中文
 * @param s
 * @returns {Boolean} 是返回TRUE不是返回FALSE
 */
function isChina(s) {
	var patrn= /[\u4E00-\u9FA5]|[\uFE30-\uFFA0]/gi;
	if (patrn.exec(s))
	{
		return true;
	}else{
		return false;
	}
} 

/**
 * @功能 判断输入字符串是否为中文、英文、中英结合
 * @param s
 * @returns {Boolean} 是返回TRUE 不是返回FALSE
 */
function isChinaEng(s)
{
	var patrn= /^[\u4e00-\u9fa5a-zA-Z]{1}$/;
	if (patrn.exec(s))
	{
		return true;
	}else{
		return false;
	}
} 

/**
 * 
 * @功能 判断是否为正确的银行卡号
 * @param s
 * @returns {Boolean} 是返回TRUE 不是返回FALSE
 */
function isBankCard(s){
	var reg= new RegExp("^[^0]\\d{14,18}$");
	var bankcard=s.replace(/[ ]/g,"");
	if (reg.test(bankcard)){
		return true;
	}
	else{
		return false;
	}
}


/**
 * @功能 日期比较
 * @要求 日期格式 ："yyyy-mm-dd"
 * @param Date1,Date2
 * @returns {number} 如果前面的日期大于后面的日期则返回结果>0, 否则<0
 */
function compareDate(Date1, Date2) {
	return beginCompareDate(Date1.replace(/-/g,"/"),Date2.replace(/-/g,"/"));
}

/**
 * @功能 判断输入是否为日期格式
 * @param mystring 名称
 * @param sm 数据
 * @returns {String} 返回msg消息
 */
function isDate(mystring,sm){//判断日期
	var msg="";
	var reg = /^(\d{4})-(\d{2})-(\d{2})$/;
	var str = mystring;
	var arr = reg.exec(str);
	if (str==""){ 
		msg="请保证"+sm+"中输入的日期格式为yyyy-mm-dd或正确的日期!\n";
		return msg;
	}
	if (!reg.test(str)&&RegExp.$2<=12&&RegExp.$3<=31){
		msg="请保证"+sm+"中输入的日期格式为yyyy-mm-dd或正确的日期!\n";
		return msg;
	}
	return msg;
}

/**
 * @功能 手机号码验证
 * @param s
 * @returns {Boolean} 正确返回TRUE 错误返回FALSE
 */
function isMobile(s){//手机
	if (s.match("^1\\d{2}\\d{8}$")){
		return true;
	}
	else{
		return false;
	}
}

/**
 * @功能 家庭座机验证
 * @param s
 * @returns {Boolean}正确返回TRUE 错误返回FALSE
 */
function isPhone(s){//座机
	  if (s.match(/^0\d{2,3}-?\d{7}$/)){
			return true;
		}
		else{
			return false;
		}
	}

/**
 * @功能 对邮箱格式进行检查
 * @param s
 * @returns {Boolean} 符合邮箱格式返回TRUE 不符合返回FALSE
 */
function isEmail(s){
	if (s.match("^([a-zA-Z0-9_\\-\\.])+@([a-zA-Z0-9_\\-])+(\\.([a-zA-Z0-9_\\-])+)*(\\.([a-zA-Z])+)$")){
		return true;
	}
	else{
		return false;
	}
}

/**
 * @功能 判断是否为空
 * @param str
 * @returns {Boolean} 是返回TRUE 不是返回FALSE
 */
function isNull(str){
	if(trim(str)==""){
		return true;
	}else{
		return false;
	}
	
}


/**
*@功能 全选
*@param checkBoxName,selectAllBoxId
*选择框name,全选框的id
*/
function checkAll(checkBoxName,selectAllBoxId){
	var checkList = document.getElementsByName(checkBoxName);
	if(document.getElementById(selectAllBoxId).checked){
	 for(var i=0 ; i< checkList.length;i++){
		checkList[i].checked = true;
	  }	
	}else{  
		for(var i=0 ; i< checkList.length;i++){
			checkList[i].checked = false;
		  }
	  }
}

/**
 * 三单退单 的全选框，只勾选未比对的
 * @param checkBoxName
 * @param selectAllBoxId
 */
function checkBillAll(checkBoxName,selectAllBoxId){
	var checkList = document.getElementsByName(checkBoxName);
	if(document.getElementById(selectAllBoxId).checked){
	 var index=0;
	 for(var i=0 ; i< checkList.length;i++){
		var value= checkList[i].value;
		var arrive=value.split(",")[1];
		if(arrive==0){
			index++;
			checkList[i].checked = true;
		}
	  }	
	 if(index!=checkList.length){
		 alert("全选只勾选未进行对比的");
		 document.getElementById(selectAllBoxId).checked=false;
	 }
	}else{  
		for(var i=0 ; i< checkList.length;i++){
			checkList[i].checked = false;
		  }
	  }
}

/**
*
*判断是否应该选中全选框
*@param checkBoxName,selectAllBoxId
*选择框name,全选框的id
*/
function ifcheckBox(checkBoxName,selectAllBoxId,status,index){	
  var checkedlist = document.getElementsByName(checkBoxName);
  if(status==1){
	  alert("清单已入库，不能退单");
	 checkedlist[index].checked = false; 	//判断是否全选 是 则全选框选中 不是则 不选中全选框
	  retrun;
  }
  	flag=true;
	 for(var i = 0;i < checkedlist.length;i++){
		 if(checkedlist[i].checked != true ){	//判断是否全选 是 则全选框选中 不是则 不选中全选框
		 	flag=false;	
		 } 
		}
	document.getElementById(selectAllBoxId).checked = flag;
}

/**
 * 判断是否有被选中
 * 
 * @returns {Boolean}
 */
function ifChecked(checkBoxName){
	var flag = false ; 
	 var checkedlist = document.getElementsByName(checkBoxName);
	 for(var i = 0;i < checkedlist.length;i++){
		 if(checkedlist[i].checked){			 
		 	 flag = true;			
		 }			 
	 }
   return flag;
}


/** 获取选行中的主键
*
*@param checkBoxName 页面中选着框的名字
*
*写入页面中 id=selectedfcNo的隐藏框的ID
*/
function getSelectedNo(checkBoxName){
	var temp=""; //临时变量
	var nos="";
	var checkList = document.getElementsByName(checkBoxName);
	for(var i=0 ; i< checkList.length;i++){
		if(checkList[i].checked ){
			temp=checkList[i].value; //将值放于临时变量temp
			nos+=temp+","; 
			}
	  }
	document.getElementById("selectedfcNo").value = nos.substring(0, nos.length-1);
}

/**
 * 出口
 * @HC
 * @param action 访问url
 * @param param 参数		
 * @param jumpAddress 跳转地址
 */
/*function ajaxRequest(action,param,urlpath,jumpAddress){
	
	$.ajax({
		url:action,
		data:{"ebcebillNo":param,"urlpath":urlpath},
		success:function(data){
			if(data!=""||data!=null){
				$("#message2").html(data);
				$("#popup").show();
				$("#alertTip2").show();
				//setTimeout("autoCloseTip('"+jumpAddress+"')",1500);
			}else{
				window.location=jumpAddress;	//直接跳转
			}
		}
	});
	
}*/

/*//pass
function allOperate(checkBoxName,action){
	if(ifChecked(checkBoxName)){
		getSelectedNo(checkBoxName);
		var param=$("#selectedfcNo").val();
		// action = "pass";
		var urlpath="中文";
		var jumpAddress="";
		iAjaxRequest(action,param,urlpath,jumpAddress);
		
	}else{
	
		$("#message").html("请选择需要操作的清单!");
		$("#popup").show();
		$("#alertTip").show();
	}
}*/
function closeTip(){
	$("#popup").hide();
	$("#alertTip").hide();
}

/**
 * 进口
 * @param action 访问url
 * @param param 参数		
 * @param jumpAddress 跳转地址
 *//*
function iAjaxRequest(action,param,urlpath,jumpAddress){
	
	$.ajax({
		url:action,
		data:{"cbecbillNo":param,"urlpath":urlpath},
		success:function(data){
			if(data!=""||data!=null){
				$("#message2").html(data);
				$("#popup").show();
				$("#alertTip2").show();
				//window.location=jumpAddress;	//直接跳转
				//setTimeout("autoCloseTip('"+jumpAddress+"')",1500);
			}else{
				window.location=jumpAddress;	//直接跳转
			}
		}
	});
	
}
*/
 
function autoCloseTip(jumpAddress){
	
	$("#message").html("");
	$("#popup").hide();
	$("#alertTip").hide();
	window.location=jumpAddress;
}

function seeCheckRd(){
	
	var number = $("#number").val();
	if(number>0){
		$("#popup").show();
		$("#seeCheck").show();
	}
}
function seeCheckRdi(){
	var number = $("#number").val();
	if(number>0){
		$("#popupi").show();
		$("#seeChecki").show();
	}/*else if(number==0){
		newCheckRd();
	}*/
	
}
function newCheckRd(){
	//ajax调用
	var ecbillNo= $("#ecbillNo").val();
	ajaxSeriNo(ecbillNo);
	var now = new Date();
    var ckTm = now.getFullYear()+"-"+((now.getMonth()+1)<10?"0":"")+
    (now.getMonth()+1)+"-"+(now.getDate()<10?"0":"")+now.getDate()+" "
    +now.getHours()+":"+now.getMinutes()+":"+now.getSeconds();
	var checkTm = $("#checkTime").text(ckTm);
	/*getHours() 返回 Date 对象的小时 (0 ~ 23)。  
	getMinutes() 返回 Date 对象的分钟 (0 ~ 59)。  
	getSeconds() 返回 Date 对象的秒数 (0 ~ 59)。*/
	
/*	$("#popup").show();
	$("#seeCheck").hide();
	$("#newCheck").show();*/
	$("#newCheck").dialog({ height:340,  width :660   });
	/*
	if (isimp==1) {
		saveRd=newSaveRd;
	}*/
}
function newCheckRdi(){
	//ajax调用
	var ecbillNo= $("#ecbillNo").val();
	ajaxSeriNo(ecbillNo);
	var now = new Date();
    var ckTm = now.getFullYear()+"-"+((now.getMonth()+1)<10?"0":"")+
    (now.getMonth()+1)+"-"+(now.getDate()<10?"0":"")+now.getDate()+" "
    +now.getHours()+":"+now.getMinutes()+":"+now.getSeconds();
	var checkTm = $("#checkTime").text(ckTm);
	/*getHours() 返回 Date 对象的小时 (0 ~ 23)。  
	getMinutes() 返回 Date 对象的分钟 (0 ~ 59)。  
	getSeconds() 返回 Date 对象的秒数 (0 ~ 59)。*/
	/*$("#popupi").show();
	
	$("#seeChecki").hide();
	$("#newChecki").show();*/
	$("#newChecki").dialog({ height:340,  width :660   });
	
	/*
	if (isimp==1) {
		saveRdi=newSaveRdi;
	}*/
}
function closeCheckRdi(){
	$("#popupi").hide();
	$("#seeChecki").hide();
	$("#newChecki").hide();
}
function dialogClose(){
	$("#newChecki").dialog('close');
}


function closeCheckRd(){
	$("#popup").hide();
	$("#seeCheck").hide();
	$("#newCheck").hide();
}

//去后台生成查验记录单号
function ajaxSeriNo(ecbillNo){
	var jumpAddr="";
	var impOrExp = $("#impOrExp").val();
	if(impOrExp=="exp"){
		jumpAddr="../expCheckManage/checkSeriNum";
	}else{
		//isimp=1;
		jumpAddr="../impCheckManage/checkSeriNum";
	}
	$.ajax({
		type:"post",
		url: jumpAddr,
		contentType:"application/x-www-form-urlencoded; charset=utf-8",
		data:{
			"cbecbillNo":ecbillNo
		},
		success:function(data){
			if(data!=null){
				$("#serNum").text(data);
			}
		}
	});
}


/**
 * 退单 布控查验  撤单 核销 暂扣
 */

function ebcOperate(type,popup,no){
	
	var IfRevocation="true";
	//type:1退单  2撤单 3布控查验 4核销 5暂扣  6立案 7结案 8 恢复  16作废  18:人工放行页面（退单）
	//popup 弹层类型1有reason 2无reason
	if(popup==1){
		if(type==1){
			$("#title").html("请输入退单意见");
			$("#type").val(1);	
			becbillNo = no;
		}else if(type==2) {
			var cebNo=$("#cbecbillNo").val();
			$.ajax({
				 type: "POST",
				    contentType:"application/x-www-form-urlencoded; charset=utf-8",
				    url:"../impAudiManage/IfRevocation",
				    data:{"cbecbillNo":cebNo},
				    async: false,
				    success: function(data) {
				    	var result=eval("("+data+")");
				    	if (result) {
				    		$("#title").html("请输入撤单意见");
				    		$("#type").val(2);
				    		becbillNo = no;
						}
				    	else {
				    		IfRevocation="false";
				    		$("#message").html("该清单中有汇总税单，不能删除");
					    	$("#alertTip").show();
						}
				    }
			});
		}else if(type==3) {
			var path=$("#urlpath").val();
			var taxStatus=$("#taxStatus").val();
			if(path=="查验管理>选择查验>清单详情" && taxStatus==2){
				//alert("申报单的税单被归并，请取消归并后再下达查验指令,下达查验指令失败");
				$("#message4").html("申报单的税单被归并，请取消归并后再下达查验指令,下达查验指令失败");
				$("#popupi").show();
				$("#alertTip4").show();
				return;
			}
			if(path=="查验管理>选择查验>清单详情" && taxStatus==3){
				alert("申报单的税单被核销，下达查验指令失败");
				return;
			} 

			$("#title").html("请输入布控意见");
			$("#type").val(3);
			becbillNo = no;
		}else if(type==4) {
			$("#title").html("请输入核销意见");
			$("#type").val(4);
			becbillNo = no;
		}else if(type==5) {
			var ebcebillNo= $("#cbecbillNo").val();
			$("#title").html("请输入暂扣理由");
			$("#whouse").html("暂扣记录编号：");
			var timestamp=new Date().getTime();
			$("#whId").html("D"+timestamp+ebcebillNo);
			$("#type").val(5);
			becbillNo = ebcebillNo;//在查验页面 暂扣后跳转 所带参数
			billNo = $("#billNo").val(); //在查验页面 暂扣后跳转 所带参数
		}else if(type==6) {
			$("#title").html("请输入立案理由");
			$("#type").val(6);
			becbillNo = no;
		}else if(type==7) {
			$("#title").html("请输入结案理由");
			$("#type").val(7);
			becbillNo = no;
		}
	else if(type==8) {
		$("#title").html("请输入恢复理由");
		$("#type").val(8);
		becbillNo = no;
	}else if(type==13) {
		//alert("11");
		$("#title").html("请输入查验指令");
		$("#type").val(13);
	}else if(type==16) {
		$("#title").html("请输入作废理由");
		$("#type").val(16);
	}else if(type==18){
		$("#title").html("请输入退单意见");
		$("#type").val(18);	
		becbillNo = no;
	}
		if (IfRevocation!="false") {
		$("#popup").show();
		$("#wh").show();
		}
	}else{// 9通过  10放行 11删单  12取消布控查验 17放行(可备注是何种方式放行)
		if(type==9){
			$("#type").val(9);
			becbillNo = no;
		}else if(type==10) {
			$("#type").val(10);
			becbillNo = no;
		}else if(type==11) {
			$("#type").val(11);
		}else if(type==12) {
			$("#type").val(12);
		}else if(type==14) {
			$("#type").val(14);
		}
		
		if(type==17){
			$("#type").val(17);
			$("#contentWh2").css({"display":"none"});
			$("#wh2Span").css({"display":"block"});
			$("#popup").show();
			$("#wh2").show();
		}else{
			$("#contentWh2").css({"display":"block"});
			$("#wh2Span").css({"display":"none"});
			$("#popup").show();
			$("#wh2").show();
		}	
	}
	
}
function cleara(){
	$("#reason").val("");
}

function ajaxReason(){
	
	//type:1退单  2撤单 3布控查验 4核销 5暂扣     6立案 7结案 8恢复
	var impOrExp = $("#impOrExp").val();
	var ebcebillNo = $("#cbecbillNo").val();
	var cbecbillNo = $("#cbecbillNo").val();
	var waybillNo = $("#waybillNo").val();
	var taxNo = $("#taxNo").val();
	var reason = $('#reason').val();
	var whId = $("#whId").html();
	var type = $("#type").val();	
	var ifCheck = $("#ifCheck").val() ;
	if(type==1){
		var ajaxCallUrl = "back";		
		jumpAddress = "manualCheck";
		if(impOrExp=="imp"){
			urlpath="审单管理>人工审单";
		}else{
			urlpath="审单管理>人工审单";
		}
		if(becbillNo != ""&&typeof(becbillNo)!="undefined"){
			ebcebillNo = becbillNo;
			cbecbillNo = becbillNo;
			becbillNo = "";
		}
	}else if(type==2){
		ajaxCallUrl = "revocation";
		jumpAddress = "declareRevoke";
		if(impOrExp=="imp"){
			urlpath="审单管理>清单删单";
		}else{
			urlpath="审单管理>清单删单";
		}
		if(becbillNo != ""&&typeof(becbillNo)!="undefined"){
			ebcebillNo = becbillNo;
			cbecbillNo = becbillNo;
			becbillNo = "";
		};
		
		
	}else if(type==3){
		var status=$("#status").val();
		urlpath=$("#urlpath").val();
	    urlpath=encodeURI(urlpath);
		if(impOrExp=="imp"){	
			ajaxCallUrl = "../impAudiManage/checkOrder?status="+status+"&urlpath="+urlpath;
			jumpAddress = "audiDetails?cbecbillNo="+cbecbillNo+"&urlpath="+urlpath;			
		}else{
			ajaxCallUrl = "../expAudiManage/checkOrder?status="+status+"&urlpath="+urlpath;
			jumpAddress = "audiDetails?fcNo="+cbecbillNo+"&urlpath="+urlpath;	
		}
	}else if(type==4){
		ajaxCallUrl = "verification";
		jumpAddress = "audiVerify";		
		urlpath="审单管理>人工核销";

	}else if(type==5){//暂扣按钮 在进出口 监控页面 查验页面的不同处理
		var currurlpath=$("#urlpath").val();
		if(impOrExp=="imp"){			
			ajaxCallUrl = "../impCheckManage/expWithHold";			
			if(currurlpath=="审单管理>选择查验>清单详情"){
				urlpath="审单管理>选择查验";
				jumpAddress = "../impCheckManage/emailMonitors?urlpath="+urlpath;
			}else{
				cbecbillNo=becbillNo;
				becbillNo = "";
				jumpAddress = "../impCheckManage/impEmailCheck?cbecbillNo="+cbecbillNo+"&&billNo="+billNo+"&&urlpath="+currurlpath;
			}
			
		}else{			
			ajaxCallUrl = "../expCheckManage/expWithHold";
			if(currurlpath=="审单管理>选择查验>清单详情"){
				urlpath="审单管理>选择查验";
				jumpAddress = "../expCheckManage/emailMonitors?urlpath="+urlpath;
			}else{
				cbecbillNo=becbillNo;
				becbillNo = "";
				jumpAddress = "../expCheckManage/expEmailCheck?cbecbillNo="+cbecbillNo+"&&billNo="+billNo+"&&urlpath="+currurlpath;
			}
		}
	}else if(type==6){
		urlpath=$("#urlpath").val();
		if(impOrExp=="imp"){
			cbecbillNo=becbillNo;
			becbillNo = "";
			ajaxCallUrl = "../impCaseManage/register";	
			jumpAddress = "../impCaseManage/caseRecords";			
		}else{
			cbecbillNo=becbillNo;
			becbillNo = "";
			ajaxCallUrl = "../expCaseManage/register";
			jumpAddress = "../expCaseManage/caseRecords";			
		}		
		
	}else if(type==7){
		urlpath=$("#urlpath").val();
		
		if(impOrExp=="imp"){
			cbecbillNo=becbillNo;
			becbillNo = "";
			ajaxCallUrl = "../impCaseManage/closedCase";
			jumpAddress = "../impCaseManage/caseManage";			
		}else{
			cbecbillNo=becbillNo;
			becbillNo = "";
			ajaxCallUrl = "../expCaseManage/closedCase";
			jumpAddress = "../expCaseManage/caseManage";			
		}	
		
	}else if(type==8){

		urlpath=$("#urlpath").val();
		if(impOrExp=="imp"){
			cbecbillNo=becbillNo;
			becbillNo = "";
			ajaxCallUrl = "../impCaseManage/recovery";
			jumpAddress = "../impCaseManage/caseManage";			
		}else{
			cbecbillNo=becbillNo;
			becbillNo = "";
			ajaxCallUrl = "../expCaseManage/recovery";
			jumpAddress = "../expCaseManage/caseManage";			
		}	
		
	}else if(type==18){
		var ajaxCallUrl = "back";		
		jumpAddress = "manualDischarged";
		if(impOrExp=="imp"){
			urlpath="查验管理>人工放行";
		}else{
			urlpath="查验管理>人工放行";
		}
		if(becbillNo != ""&&typeof(becbillNo)!="undefined"){
			ebcebillNo = becbillNo;
			cbecbillNo = becbillNo;
			becbillNo = "";
		}
	}
	
	$.ajax({
	    type: "POST",
	 
	    contentType:"application/x-www-form-urlencoded; charset=utf-8",
	    url:ajaxCallUrl,
	    data:{"waybillNo":waybillNo,"ebcebillNo":ebcebillNo,"reason":encodeURI(reason),"detainNo":whId,"taxNo":taxNo,
	    	"cbecbillNo":cbecbillNo,"ifCheck":ifCheck},
	    async: false,
	    success: function(data) {
	    	$("#wh").hide();
	    	$("#wh2").hide();
	    	$("#message2").html(data);
	    	$("#alertTip2").show();
	    	
	    }
	});
}

function ajaxConfirm(){ // 9通过  10放行  11删单  12取消布控查验 17放行（可选放行方式）
	
	var ebcebillNo = $("#cbecbillNo").val();
	var cbecbillNo = $("#cbecbillNo").val();
	var waybillNo = $("#waybillNo").val();
	var content = $('#popupForm2').serialize();	
	var type = $("#type").val();
	var impOrExp = $("#impOrExp").val();
	var dischargeWay = $("#dischargeWay").val();
	var undoControl = $("#undoControl").val();
	
	
	if(type==9){
		var ajaxCallUrl = "pass";
		jumpAddress = "manualCheck";
		if(impOrExp=="imp"){
			urlpath="审单管理>人工审单";
		}else{
			urlpath="审单管理>人工审单";
		}
		if(becbillNo != ""&&typeof(becbillNo)!="undefined"){
			ebcebillNo = becbillNo;
			cbecbillNo = becbillNo;
			becbillNo = "";
		};
	}else if(type==10){
		ajaxCallUrl = "discharged";		
		jumpAddress = "manualDischarged";
		if(impOrExp=="imp"){
			urlpath="查验管理>人工放行";
		}else{
			urlpath="查验管理>人工放行";
		}
		if(becbillNo != ""&&typeof(becbillNo)!="undefined"){
			ebcebillNo = becbillNo;
			cbecbillNo = becbillNo;
			becbillNo = "";
		};
	}else if(type==11){
		ajaxCallUrl = "back";
		jumpAddress = "manualDischarged";
		if(impOrExp=="imp"){
			urlpath="查验管理>人工放行";
		}else{
			urlpath="查验管理>人工放行";
		}
	}else if(type==12){
		ajaxCallUrl = "cancelCheckOrder";	
		urlpath=$("#urlpath").val();
		if(impOrExp=="imp"){			
			jumpAddress = "audiDetails?cbecbillNo="+cbecbillNo+"&urlpath="+urlpath;			
		}else{
			jumpAddress = "audiDetails?fcNo="+cbecbillNo+"&urlpath="+urlpath;	
		}
	}/*else if(type==3){
		
		var status=$("#status").val();
		urlpath=$("#urlpath").val();
		ajaxCallUrl = "checkOrder?status="+status+"&urlpath="+urlpath;
		
		if(impOrExp=="imp"){			
			jumpAddress = "audiDetails?cbecbillNo="+cbecbillNo+"&urlpath="+urlpath;			
		}else{
			jumpAddress = "audiDetails?fcNo="+cbecbillNo+"&urlpath="+urlpath;	
		}
	}*/else if(type==17){
		ajaxCallUrl = "discharged";		
		jumpAddress = "manualDischarged";
		if(impOrExp=="imp"){
			urlpath="查验管理>人工放行";
		}else{
			urlpath="查验管理>人工放行";
		}
		if(becbillNo != ""&&typeof(becbillNo)!="undefined"){
			ebcebillNo = becbillNo;
			cbecbillNo = becbillNo;
			becbillNo = "";
		};
	}
	$.ajax({
	    cache: true,
	    type: "POST",
	    url:ajaxCallUrl,
	    contentType:"application/x-www-form-urlencoded; charset=utf-8",
	    data:{"waybillNo":waybillNo,"ebcebillNo":ebcebillNo,"content":content,
	    	"cbecbillNo":cbecbillNo,"dischargeWay":dischargeWay,"undoControl":undoControl},// 你的formid
	    async: false,
	    success: function(data) {
	    	$("#wh").hide();
	    	$("#wh2").hide();    	
	    	$("#message2").html(data);
	    	$("#alertTip2").show();	
	    }
	});
}
function ok(){//提示弹出层
	var type = $("#type").val();
	var cbecbillNo = $("#cbecbillNo").val();
	var impOrExp = $("#impOrExp").val();
	if (type==3) {
		window.location=jumpAddress;				
	}else if (type==5){
		/*if(impOrExp=="exp"){
			urlpath="审单管理>选择查验";
			jumpAddress="../expCheckManage/emailMonitors";
		}else{
			urlpath="审单管理>选择查验";
			jumpAddress="../impCheckManage/emailMonitors";
		}
		window.location=jumpAddress+"?urlpath="+urlpath ;*/
		window.location=jumpAddress;
	}else if(type==12){
		if(urlpath == "查验管理>待撤销列表>清单详情"){
			if(impOrExp=="exp"){
				jumpAddress="../expCheckManage/eCancelCheckList?urlpath="+urlpath;
			}else{
			jumpAddress="../impCheckManage/iCancelCheckList?urlpath="+urlpath;
			}
		}
		window.location=jumpAddress ;
	}else if(type=="checkRecord"){
		urlpath=$("#urlpath").val();
		if(urlpath == "查验管理>待查验列表>清单详情" || urlpath == "查验管理>待查验列表"){
			urlpath = "查验管理>待查验列表";
			if(impOrExp=="exp"){
				jumpAddress="../expCheckManage/expWaitCheckList?urlpath="+urlpath;
			}else{
				jumpAddress="../impCheckManage/iWaitCheckList?urlpath="+urlpath;
			}
		}else if(urlpath == "查验管理>查验记录单修改"){
			urlpath = "查验管理>查验记录单修改";
			if(impOrExp=="exp"){
				jumpAddress="../expCheckManage/checkModifyList?urlpath="+urlpath;
			}else{
			jumpAddress="../impCheckManage/checkModifyList?urlpath="+urlpath;
			}
		}else{
			if(impOrExp=="exp"){
				jumpAddress="../expCheckManage/expEmailCheck?cbecbillNo="+cbecbillNo+"&urlpath="+urlpath;
			}else{
				jumpAddress="../impCheckManage/impEmailCheck?cbecbillNo="+cbecbillNo+"&urlpath="+urlpath;
			}
		}

		window.location=jumpAddress ;
	}else if(!isNaN(type)){
		window.location=jumpAddress+"?urlpath="+urlpath ;			
	}
}
function popupHide(){
	$("#popup").hide();
	$("#wh").hide();
	$("#wh2").hide();
}

function goBack(){
	history.go(-1);
}


/**
 * 检查是否有锁
 * @param cbecbillNo
 * @param addrType:1-人工审单，2-待查验列表，3-人工放行，4-暂扣处理 5-选择查验 6-撤销布控
 */
function checkLock(cbecbillNo,urlpath,addrType,id,condition){
	
	$.ajax({
	    type: "POST",
	    url:"../cmnLock/checkLock",
	    contentType:"application/x-www-form-urlencoded; charset=utf-8",
	    data:{"cbecbillNo":cbecbillNo},// 你的formid
	    async: false,
	    success: function(data) {
	    	lockInfo(data,cbecbillNo,urlpath,addrType,id,condition);
	    }
	});
	
}


/**
*判断锁的信息 做提示或者跳转
**/
function lockInfo(data,fcNo,urlpath,addrType,id,condition){
	var arr=data.split(".");
	var info=arr[0];
	var operator=arr[1];
	if(info=="locked"){
	    $("#popup").show();
		$("#message3").html("此包裹正被关员:"+operator+"操作中！");
	 	$("#alertTip3").show();
	}
	if(info=="unlocked"){
		var impOrExp = $("#impOrExp").val();
		//id:有值表示为清单修改与清单删单页面（跳转jsp为AudiDetail2）
		if(id==""){
			if(impOrExp == "exp" ){
				window.location="../expAudiManage/audiDetails?fcNo="+fcNo+"&urlpath="+urlpath+">清单详情&addrType="+addrType;
			}else if(impOrExp=="imp"){
				window.location="../impAudiManage/audiDetails?cbecbillNo="+fcNo+"&urlpath="+urlpath+">清单详情&addrType="+addrType+"&condition="+condition;
			}else if(impOrExp=="impDetain"){
				window.location="../impCheckManage/cbecdetail?cbecbillNo="+fcNo+"&urlpath="+urlpath+">清单详情";
			}else if(impOrExp=="expexception"){
				window.location="../expAudiManage/exceptionBillDetail?cbecbillNo="+fcNo+"&urlpath="+urlpath+">异常清单详情&addrType="+addrType;
			}else if(impOrExp=="expEdit"){
				window.location="../expAudiManage/editDetail?cbecbillNo="+fcNo+"&urlpath="+urlpath+"清单详情&addrType="+addrType;
			}else if(impOrExp=="impEdit"){
				window.location="../impAudiManage/editDetail?cbecbillNo="+fcNo+"&urlpath="+urlpath+">清单详情&addrType="+addrType;
			}else{
				window.location="../impAudiManage/exceptionBillDetail?cbecbillNo="+fcNo+"&urlpath="+urlpath+">异常清单详情&addrType="+addrType;
			}
		}else{
			if(impOrExp == "exp" ){
				window.location="../expAudiManage/audiDetails?fcNo="+fcNo+"&urlpath="+urlpath+">清单详情&addrType="+addrType+"&id="+id;
			}else if(impOrExp=="imp"){
				window.location="../impAudiManage/audiDetails?cbecbillNo="+fcNo+"&urlpath="+urlpath+">清单详情&addrType="+addrType+"&id="+id;
			}
		}
		
		
	}

}












//JavaScript Document
//基础功能函数


charset="utf-8";

/**  
* 判断身份证号码为18位时最后的验证位是否正确  
* @param a_idCard 身份证号码数组  
* @return  
*/  
function isTrueValidateCodeBy18IdCard(a_idCard) {   
	var sum = 0;                             // 声明加权求和变量   
	if (a_idCard[17].toLowerCase() == 'x') {   
		a_idCard[17] = 10;                    // 将最后位为x的验证码替换为10方便后续操作   
	}   
	for ( var i = 0; i < 17; i++) {   
		sum += Wi[i] * a_idCard[i];            // 加权求和   
	}   
	valCodePosition = sum % 11;                // 得到验证码所位置   
	if (a_idCard[17] == ValideCode[valCodePosition]) {   
		return true;   
	} else {   
		return false;   
	}   
}   


/**  
* 验证18位数身份证号码中的生日是否是有效生日  
* @param idCard 18位书身份证字符串  
* @return  
*/  
function isValidityBrithBy18IdCard(idCard18){   
	var year =  idCard18.substring(6,10);   
	var month = idCard18.substring(10,12);   
	var day = idCard18.substring(12,14);   
	var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));   
	// 这里用getFullYear()获取年份，避免千年虫问题   
	if(temp_date.getFullYear()!=parseFloat(year)   
			||temp_date.getMonth()!=parseFloat(month)-1   
			||temp_date.getDate()!=parseFloat(day)){   
		return false;   
	}else{   
		return true;   
	}   
}   
/**  
* 验证15位数身份证号码中的生日是否是有效生日  
* @param idCard15 15位书身份证字符串  
* @return  
*/  
function isValidityBrithBy15IdCard(idCard15){   
	var year =  idCard15.substring(6,8);   
	var month = idCard15.substring(8,10);   
	var day = idCard15.substring(10,12);   
	var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));   
	// 对于老身份证中的你年龄则不需考虑千年虫问题而使用getYear()方法   
	if(temp_date.getYear()!=parseFloat(year)   
			||temp_date.getMonth()!=parseFloat(month)-1   
			||temp_date.getDate()!=parseFloat(day)){   
		return false;   
	}else{   
		return true;   
	}   
}

/**
*@功能 执行日期比较
* @param sTime1
* @param sTime2
* @returns {Number}
*/
function beginCompareDate(sTime1,sTime2) {
	t1 = Date.parse(sTime1);
	t2 = Date.parse(sTime2);
	return t1 - t2;
}

/**
*@功能 全选判断
*
*
*/



/**
* 
* @功能 计算字符串长度
* @param str 字符串
* @returns {Number}
*/
function ansiLength(str){
	var len=0;
	if(str.length>0){
		for (var i=0; i<str.length; i++) {
	        var charCode = str.charCodeAt(i);
	        if (charCode <= 0x0000007f){
	            len += 1;
	        } else if (charCode >= 0x00000080 && charCode <= 0x000007FF) {
	            len += 2;
	        } else if (charCode >= 0x00000800 && charCode <= 0x0000FFFF) {
	            len += 3;
	        } else if (charCode >= 0x00010000 && charCode <= 0x001FFFFF) {
	            len += 4;
	        } else if (charCode >= 0x00200000 && charCode <= 0x03FFFFFF) {
	            len += 5;
	        } else if (charCode >= 0x04000000  && charCode <= 0x7FFFFFFF) {
	            len += 6;
	        }
	    }
	}
	return len;
}





//JavaScript Document
//基础功能函数


charset="utf-8";

/**  
* 判断身份证号码为18位时最后的验证位是否正确  
* @param a_idCard 身份证号码数组  
* @return  
*/  
function isTrueValidateCodeBy18IdCard(a_idCard) {   
	var sum = 0;                             // 声明加权求和变量   
	if (a_idCard[17].toLowerCase() == 'x') {   
		a_idCard[17] = 10;                    // 将最后位为x的验证码替换为10方便后续操作   
	}   
	for ( var i = 0; i < 17; i++) {   
		sum += Wi[i] * a_idCard[i];            // 加权求和   
	}   
	valCodePosition = sum % 11;                // 得到验证码所位置   
	if (a_idCard[17] == ValideCode[valCodePosition]) {   
		return true;   
	} else {   
		return false;   
	}   
}   


/**  
* 验证18位数身份证号码中的生日是否是有效生日  
* @param idCard 18位书身份证字符串  
* @return  
*/  
function isValidityBrithBy18IdCard(idCard18){   
	var year =  idCard18.substring(6,10);   
	var month = idCard18.substring(10,12);   
	var day = idCard18.substring(12,14);   
	var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));   
	// 这里用getFullYear()获取年份，避免千年虫问题   
	if(temp_date.getFullYear()!=parseFloat(year)   
			||temp_date.getMonth()!=parseFloat(month)-1   
			||temp_date.getDate()!=parseFloat(day)){   
		return false;   
	}else{   
		return true;   
	}   
}   
/**  
* 验证15位数身份证号码中的生日是否是有效生日  
* @param idCard15 15位书身份证字符串  
* @return  
*/  
function isValidityBrithBy15IdCard(idCard15){   
	var year =  idCard15.substring(6,8);   
	var month = idCard15.substring(8,10);   
	var day = idCard15.substring(10,12);   
	var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));   
	// 对于老身份证中的你年龄则不需考虑千年虫问题而使用getYear()方法   
	if(temp_date.getYear()!=parseFloat(year)   
			||temp_date.getMonth()!=parseFloat(month)-1   
			||temp_date.getDate()!=parseFloat(day)){   
		return false;   
	}else{   
		return true;   
	}   
}

/**
*@功能 执行日期比较
* @param sTime1
* @param sTime2
* @returns {Number}
*/
function beginCompareDate(sTime1,sTime2) {
	t1 = Date.parse(sTime1);
	t2 = Date.parse(sTime2);
	return t1 - t2;
}

/**
*@功能 全选判断
*
*
*/



/**
* 
* @功能 计算字符串长度
* @param str 字符串
* @returns {Number}
*/
function ansiLength(str){
	var len=0;
	if(str.length>0){
		for (var i=0; i<str.length; i++) {
	        var charCode = str.charCodeAt(i);
	        if (charCode <= 0x0000007f){
	            len += 1;
	        } else if (charCode >= 0x00000080 && charCode <= 0x000007FF) {
	            len += 2;
	        } else if (charCode >= 0x00000800 && charCode <= 0x0000FFFF) {
	            len += 3;
	        } else if (charCode >= 0x00010000 && charCode <= 0x001FFFFF) {
	            len += 4;
	        } else if (charCode >= 0x00200000 && charCode <= 0x03FFFFFF) {
	            len += 5;
	        } else if (charCode >= 0x04000000  && charCode <= 0x7FFFFFFF) {
	            len += 6;
	        }
	    }
	}
	return len;
}


/**
 * 控制弹出层居中
 * popupName:$("#addMegerTax")
 */
function popupSize(popupName){ 
	var _scrollHeight = $(document).scrollTop();//获取滚动条到顶部的垂直高度 
	var _windowHeight = $(window).height();//获取当前窗口高度 
	var _windowWidth = $(window).width();//获取当前窗口宽度 
	var _popupWidth = popupName.width();//获取弹出层宽度 
	var _popupHeight = popupName.height();//获取弹出层宽度 
	var _posiTop = (_windowHeight - _popupHeight)/2 + _scrollHeight; 
	var _posiLeft = (_windowWidth - _popupWidth)/2 ; 
	popupName.css({"left": _posiLeft + "px","top":_posiTop + "px","display":"block"});//设置position 
} 


/***********
 * 主单核销
 * ***********/

function operationComp(type, reason){
	$("#type").val(type);
//	$("#reason").val(reason);
	$("#popup").show();
	$("#wh4").show();
}

function ajaxConfirmComp(){
	var requestNo = $("#parentBillNo").val();
	var type = $("#type").val();
//	var reason = $("#reason").val();
	var ajaxCallUrl;
	if(type=="verification"){
		ajaxCallUrl = "verification";
		jumpAddress = "parentbillDischarged";
		urlpath="审单管理>主单核销";
	}
	
	if(type=="pverification"){
		ajaxCallUrl = "pverification";
		jumpAddress = "parentbillDischarged";
		urlpath="审单管理>主单核销";
	}
	
	$.ajax({
	    cache: true,
	    type: "POST",
	    url:ajaxCallUrl,
	    contentType:"application/x-www-form-urlencoded; charset=utf-8",
	    data:{"requestNo":requestNo,"urlpath":urlpath},// 你的formid
	    async: false,
	    success: function(data) {
	    	$("#wh").hide();
	    	$("#wh4").hide();    	
	    	$("#message4").html(data);
	    	$("#alertTip4").show();	
	    }
	});
}

function popupHideComp(){
	$("#popup").hide();
	$("#wh").hide();
	$("#wh4").hide();
}

function ok2(){//提示弹出层
	var type = $("#type").val();
	if(type != null && type != ""){
		window.location=jumpAddress+"?urlpath="+urlpath;	
	}
}




