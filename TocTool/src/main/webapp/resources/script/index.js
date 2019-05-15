$(function () {
    //左边菜单
    $('.one').click(function () {//点击事件。$()这个方法是在DOM中使用过于频繁的document.getElementById()方法的一个便利的简写
        $('.one').removeClass('one-hover');//点击class为one的标签。移除one-hover的css样式
        $(this).addClass('one-hover');//添加one-hover样式
        var $ul = $(this).parent().find('.kid');//获取class为kid的父节点赋值给变量ul  
        $(".kid").slideUp();//升起
		if($ul.is(':visible')){//如果ul属性书可见的，就升起，否则就关闭
			$ul.slideUp();
		}else{
			$ul.slideDown();
		}
    });
    $('.lia').mouseenter(function(){  // 当鼠标指针进入（穿过）元素时，改变元素的背景色： 
    	$(this).animate({
    	    opacity:0.5 //透明度为0.5 标准为1
    	},500);//500毫秒
	});
    $('.lia').mouseover(function(){  //鼠标位于元素上方时触发的事件  
    	$(this).animate({
    		opacity:1
    	},300);
    });
    //隐藏菜单
    var l = $('.left_c');
    var r = $('.right_c');
    var c = $('.Conframe');
    $('.nav-tip').click(function () {//滚动条
        if (l.css('left') == '8px') {//float 8px；
            l.animate({
                left: -300
            }, 500);
            r.animate({
                left: 21
            }, 500);
            c.animate({
                left: 29
            }, 500);
            $(this).animate({
                "background-position-x": "-12"//背景定位x-轴
            }, 300);
        } else {
            l.animate({
                left: 8
            }, 500);
            r.animate({
                left: 240
            }, 500);
            c.animate({
                left: 230
            }, 500);
            $(this).animate({
                "background-position-x": "0"
            }, 300);
        };
    })
    //横向菜单
    $('.top-menu-nav li').click(function () {
        $('.kidc').hide();
        $(this).find('.kidc').show();
        
    })
    $('.kidc').bind('mouseleave', function () {
        $('.kidc').hide();
    }) 
})
$(document).ready(function(){  
    $(document).bind("contextmenu",function(e){   
          return false;   
    });
});