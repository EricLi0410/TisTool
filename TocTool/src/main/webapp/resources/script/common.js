function dwz(id) {
    var obj=null;
    if (typeof id == 'string'){
        obj = $(id,navTab.getCurrentPanel());
    }
    return obj;
}
function dg(id){
	var obj=null;
    if (typeof id == 'string'){
        obj = $(id,$.pdialog.getCurrent());
    }
    return obj;
}
$(function (){
	$("input[type='reset']",navTab.getCurrentPanel()).click(function(){
		dwz('.searchContent input').attr("value","");
		dwz('.searchContent select').attr("value","");
		dwz('.selectContent select option:first').prop("selected",'selected');
	});
});
function setDisabled(){
	dg(".pageFormContent input[type='text']").attr("disabled",true).attr("readonly",true).removeClass("required");
	dg(".pageFormContent input[type='radio']").attr("disabled",true).attr("readonly",true).removeClass("required");
	dg(".pageFormContent input[type='number']").attr("disabled",true).attr("readonly",true).removeClass("required");
	dg(".pageFormContent select").attr("disabled",true).attr("readonly",true).removeClass("required");
}
function dialogReloadNavTab(json){
    DWZ.ajaxDone(json);
    var tabId = $("ul.navTab-tab li.selected").attr("tabid");
    if (json.statusCode == DWZ.statusCode.ok){
        if (json.navTabId || tabId!=null){
            navTab.reload(json.forwardUrl, {navTabId: json.navTabId});
        } else if (json.rel) {
            var $pagerForm = $("#pagerForm", navTab.getCurrentPanel());
            var args = $pagerForm.size()>0 ? $pagerForm.serializeArray() : {}
            navTabPageBreak(args, json.rel);
        }
        if ("closeCurrent" == json.callbackType) {
            $.pdialog.closeCurrent();
        }
    }
}
/************************************字符处理**********************************/
/**
 * 替换字符串 
 */
function Repalce(str,oldStr,newStr)
{
    var reg = eval("/"+oldStr+"/g");   
    return str.replace(reg,newStr); 
}
/**
 * 截取左边字符串
 * @param str 要截取的字符串
 * @param n 截取长度
 */
function Left(str,n){
    if(str.length > 0){
        if(n>str.length) n = str.length;
        return str.substr(0,n);
    }
    return;
}

/**
 * 截取右边字符串
 * @param str 要截取的字符串
 * @param n 截取长度
 */
function Right(str,n){
    if(str.length > 0){
        if(n>=str.length) return str;
        return str.substr(str.length-n,n);
    }
    return;
}
/**清除两边空格*/
function Trim(str){
    if (typeof str == 'string') return str.replace(/(^\s*)|(\s*$)/g, '');
}

/**LTrim:清除左边的空格 */
function Ltrim(str){ 
    if (typeof str == 'string') return str.replace(/(^\s*)/g, '');
}

/**RTrim: 清除右边的空格*/ 
function Rtrim(str){ 
    if (typeof str == 'string') return str.replace(/(\s*$)/g, '');
}
/**清除前后的非字符*/
function Strip(str) {
    if (typeof str == 'string') return str.replace(/^\s+/, '').replace(/(^\s*)|(\s*$)/g, '');
}
/**过滤字符里面的HTML标签*/
function StripTags(str) {
    if (typeof str == 'string')return str.replace(/<\/?[^>]+>/gi, '').replace(/(^\s*)|(\s*$)/g, '');
}
/***********************************验证类函数**********************************/
/**检测字符长度
    @str        字符集
    @s          开始长度
    @l          结束长度
*/
function IsLen(str,s,l){
    str=Trim(str);
    if(str.length>s && str.length<l){return str;}else{return false;}
}
/**是否是数字型数据*/
function IsNumber(str){
    if (/^\d+$/.test(str)){return str;}else{return false;}
}
/**是否是自然数型数据*/
function IsInt(str){
    if (/^(\+|-)?\d+$/.test(str)){return str;}else{return false;}
}
/**是否为字母和数字（字符集）*/
function IsLetters(str){
     if (/^[A-Za-z0-9]+$/.test(str)){return str;}else{return false;}
}
/**是否为英文字母（字符集）*/
function IsLetter(str){
     if (/^[A-Za-z]+$/.test(str)){return str;}else{return false;}
}
/**是否为大写字母（字符集）*/
function IsUpper(str){
     if (/^[A-Z]+$/.test(str)){return str;}else{return false;}
}
/**是否为小写字母（字符集）*/
function IsLower(str){
     if (/^[a-z]+$/.test(str)){return str;}else{return false;}
}
/**是否为正确的网址*/
function IsUrl(str){
    var myReg = "((http|ftp|https)://)(([a-zA-Z0-9\._-]+\.[a-zA-Z]{2,6})|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\&%_\./-~-]*)?";
    if(myReg.test(str)){return str;}else{return false;};
}
/**是否为正确的Email形式*/
function IsEmail(str)
{
    var myReg = /^([-_A-Za-z0-9\.]+)@([_A-Za-z0-9]+\.)+[A-Za-z0-9]{2,3}$/;    
    if(myReg.test(str)){return str;}else{return false;};
}
/**  是否为正确的手机号码*/
function IsMobile(str){
    var regu =/(^[1][3][0-9]{9}$)|(^0[1][3][0-9]{9}$)/;   
    var re = new RegExp(regu);   
    if (re.test(str)){return str;}else{return false;};
};