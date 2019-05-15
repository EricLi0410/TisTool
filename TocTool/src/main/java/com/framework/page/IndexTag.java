package com.framework.page;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class IndexTag extends SimpleTagSupport {
	private BasePagination<?> pagination;
	private String href;
    @Override  
    public void doTag() throws JspException, IOException {  
        JspWriter out = getJspContext().getOut();
        StringBuffer sb = new StringBuffer();
        sb.append("<div class=\"news_page\">");
        sb.append("<input type=\"hidden\" id=\"currentPage\" name=\"pageNum\" value=\""+pagination.getPageNum()+"\"/>");
        sb.append("<a onclick=\"onFirstPage()\" href=\"javascript:void(0)\" class=\"pre\">首页</a>");
        sb.append("<a onclick=\"toPrePage()\" href=\"javascript:void(0)\" class=\"pre\">上一页</a>");
        
        if(pagination.getTotalPage() > 5){
			if(pagination.getPageNum() > 2){
				for (int i = 6; i > 1; i--) {
					String css = "";
					if((i-4) == 0)
						css = "class=\"now\"";
					sb.append("<a href=\"#\" onclick=\"toCurrtPage(this,"+(pagination.getPageNum() - (i-4))+")\" "+css+">"+(pagination.getPageNum() - (i-4))+"</a>");
					css = "";
					if((pagination.getPageNum()) - (i-4) == pagination.getTotalPage())
						break;
				}
			}else{
				for (int i = 1; i < 6; i++) {
					String css = "";
					if(i == pagination.getPageNum())
						css = "class=\"now\"";
					sb.append("<a href=\"#\" onclick=\"toCurrtPage(this,"+i+")\" "+css+">"+i+"</a>");
					css = "";
					if(i == pagination.getTotalPage())
						break;
				}
			}
		}else{
			for (int i = 1; i <= pagination.getTotalPage(); i++) {
				String css = "";
				if(i == pagination.getPageNum())
					css = "class=\"now\"";
				sb.append("<a href=\"#\" onclick=\"toCurrtPage(this,"+i+")\" "+css+">"+i+"</a>");
				css = "";
				if(i == pagination.getTotalPage())
					break;
			}
		}
        sb.append("<a onclick=\"toNextPage()\" href=\"javascript:void(0)\" class=\"next\">下一页</a>");
        sb.append("<a onclick=\"toLastPage()\" href=\"javascript:void(0)\" class=\"pre\">尾页</a>");
        sb.append("</div>");
		sb.append("<script language=\"JavaScript\" type=\"text/javascript\">");
		sb.append("var currentPage = Number('"+pagination.getPageNum()+"');");
		sb.append("var totalPages = Number('"+pagination.getTotalPage()+"');");
		sb.append("function onFirstPage(){setValue(1);}");
		sb.append("function toPrePage(){if(currentPage == 1){return;}else{currentPage = currentPage-1;}setValue(currentPage);}");
		sb.append("function toNextPage(){if(currentPage == totalPages){return;}else{setValue(currentPage+1);}}	");
		sb.append("function toLastPage(){if(currentPage == totalPages){return;}else{setValue(totalPages);}}");
		sb.append("function toSubmit(){var current = document.getElementById(\"currentPage\").value;var reg = new RegExp('^[0-9]*$');if (!reg.test(current)) {");
		sb.append("artmsg(0,'您输入页数不合法!');document.getElementById(\"currentPage\").value = '"+pagination.getPageNum()+"';");
		sb.append("}else{var num = Number(current);if(num > totalPages){document.getElementById(\"currentPage\").value = totalPages;return;}setValue(current);}}");
		sb.append("function setValue(page){document.getElementById(\"currentPage\").value = page;document.forms[0].submit();}");
		sb.append("function toCurrtPage(obj,num){obj.setAttribute(\"class\",\"now\");setValue(num);}");
		sb.append("$(function(){$(document.forms[0]).bind(\"submit\", function(){toSubmit()})})");
		sb.append("</script>");
		try {
			// 使用out内置对象在页面上进行输出
			out.print(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
        super.doTag();  
    }
    
	public BasePagination<?> getPagination() {
		return pagination;
	}

	public void setPagination(BasePagination<?> pagination) {
		this.pagination = pagination;
	}

	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}  
}
