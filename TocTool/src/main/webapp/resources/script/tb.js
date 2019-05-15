 /* jqeury-CcUi 0.1
 * Copyright (c) 2012 Chuchur  http://www.Chuchur.com/
 * Date: 2012-6-25
 * 使用tableUI可以方便地将表格提示使用体验。先提供的功能有奇偶行颜色交替，鼠标移上高亮显示
 */
(function () {
    // 可创建一个新的jQuery副本，不影响原有的jQuery对像。
    var plugin = jQuery.sub();
    // 扩展该副本与新的插件方法
    plugin.fn.extend({
        tableUi: function (options) {
            var defaults = {
                thCss: 'thCss', //表头的背景颜色
                evenRowCss: 'treven', //奇数行的CSS
                oddRowCss: 'trodd',   //偶数行的CSS`
                activeCss: 'tractive'  //鼠标访问的CSS
            }
            var options = $.extend(defaults, options);
            return this.each(function () {
                var o = options;
                var tb = $(this);
                //添加奇偶行颜色
                tb.find("tr:even").addClass(o.evenRowCss);
                tb.find("tr:odd").addClass(o.oddRowCss);
                tb.find("th").addClass(o.thCss);
                //添加活动行颜色
                tb.find("tr").bind("mouseover", function () {
                    $(this).addClass(o.activeCss);
                });
                tb.find("tr").bind("mouseout", function () {
                    $(this).removeClass(o.activeCss);
                });
            })
        },
        tabs: function (options) { 
		var defaults = {
                MouseMode: 'mouseover' //鼠标操作绑定类型，当然你可以设为click..更多..
            }
            var options = $.extend(defaults, options);
            return this.each(function () {
				var o = options;
				var tabs = $(this);
				tabs.find('.list').not(':first').hide();
				var _nav = tabs.find('.nav a');
				_nav.eq(0).addClass('myon');
				var _panle = tabs.find('.list');
				$(_nav,tabs).bind(o.MouseMode,function(){
					var _index = $(_nav,tabs).index(this);
					_panle.hide().eq(_index).show();
					$(_nav).removeClass('myon').eq(_index).addClass('myon');
					}) 
				})
        }
    });
    // 把新的插件加入到源
    jQuery.fn.Chuchur = function () {
        this.addClass("plugin");
        // 确保我们的插件返回我们特殊的jQuery插件版本
        return plugin(this);
    };
})();// JavaScript Document
$(function(){
	$('.tb').Chuchur().tableUi();
	$('input[type="reset"]').click(function(){
    	$(".tbform input[type='text']").attr("value","");//清空  
    	$(".tbform select").attr("value","");//赋值  
    	return false;
    });
})

var re_seach = /(^\s+)|(\s+$)/g;
//弹出div
function opendiv(title, context, width, height) {
	var wid = '500px';
	if (width != null && width != '')
		wid = width;

	var hei = '240px';
	if (height != null && height != '')
		hei = height;

	layer.open({
		type : 1,
		title : title,
		skin : 'layui-layer-rim', //加上边框
		area : [ wid, hei ], //宽高
		content : context
	});
}
//弹出iframe
function openiframe(title, url, width, height) {
	var wid = '600px';
	if (width != null && width != '')
		wid = width;

	var hei = '260px';
	if (height != null && height != '')
		hei = height;
	layer.open({
		type : 2,
		title : title,
		area : [ wid, hei ], //宽高
		fix : false, //不固定
		maxmin : true,
		content : url
	});
}
function load() {
	var index = layer.load(1, {
		shade : [ 0.5, '#000000' ]
	//0.1透明度的白色背景
	});
}
function checkAll(e, itemName) {
	var Item = itemName.getElementsByTagName("input");
	var len = Item.length;
	for ( var i = 0; i < len; i++) {
		if (Item[i].type == "checkbox") {
			Item[i].checked = e.checked;
		}
	}
}

function checkOne(e, itemName) {
	var Item = itemName.getElementsByTagName("input");
	var len = Item.length;
	var b = false;
	for ( var i = 0; i < len; i++) {
		if (Item[i].type == "checkbox" && i > 0) {
			if (!Item[i].checked) {
				b = true;
				break;
			}
		}
	}
	if (b)
		Item[0].checked = false;
	else
		Item[0].checked = true;
}
function getCheck(){
	var array = new Array();
	var index = 0; 
	$('#dataDetails tbody input[name="ids"]').each(function(){
	    if(this.checked){
	    	array[index] = $(this).val(); 
	    	index ++;
	    }
	});
	return array;
}
function setDisabled(){
	$(" input[type='text']").attr("disabled",true).attr("readonly",true);
	$(" input[type='radio']").attr("disabled",true).attr("readonly",true);
	$(" input[type='checkbox']").attr("disabled",true).attr("readonly",true);
	$(" input[type='number']").attr("disabled",true).attr("readonly",true);
	$(" select").attr("disabled",true).attr("readonly",true);
}
/**提示信息*/
function artmsg(_index, _msg) {
	layer.alert(_msg, {
		icon : _index,
		skin : 'layer-ext-moon'
	})
}
function tips(tl,d){
   if(tl != ''){
	   layer.tips(tl, d, {
		   tips: [1, '#0FA6D8'],
		   maxWidth: 300,
		   time:0
	   });
   };
}
var t;
function tips(tl,d){
   if(tl != ''){
	   t = layer.tips(tl, d, {
		   tips: [1, '#0FA6D8'],
		   maxWidth: 300,
		   time:0
	   });
   };
}
function tipsout(tl){
	if(tl != ''){
		layer.close(t);
	};
}
$(document).ready(function(){  
    $(document).bind("contextmenu",function(e){   
          return false;   
    });
});