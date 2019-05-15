package com.basic.controller;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.framework.common.AjaxUtils;
import com.framework.common.DwzAjaxResult;
import com.framework.common.SpringContextUtil;
import com.framework.common.StringUtils;
import com.framework.page.BasePagination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.NoSuchMessageException;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
* @ClassName: BaseController
* @Description:  (controller基类)
* @author Meiqq
* @date 2017年12月23日 下午4:33:08
* @param <T>
*/

public abstract class BaseController<T>{
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * ThreadLocal确保高并发下每个请求的request，response都是独立的
     */
    private static ThreadLocal<ServletRequest> currentRequest = new ThreadLocal<ServletRequest>();
    private ThreadLocal<ServletResponse> currentResponse = new ThreadLocal<ServletResponse>();
    private ThreadLocal<BasePagination<T>> currentPagination = new ThreadLocal<BasePagination<T>>();
    private String jsonpCallback = null;
	/**
	 * 返回成功状态
	 */
	protected final String MSG_OPERATION_SUCCESS = "msg.operation.success";
	/**
	 * 返回失败状态
	 */
	protected final String MSG_OPERATION_FAILURE = "msg.operation.failure";
	/**
	 * 返回存在状态
	 */
	protected final String MSG_OPERATION_EXISTS = "msg.operation.exists";
	
	public static final String DEFAULT_USEROBJCET = "DEFAULT_USEROBJCET";
	public static final String DEFAULT_GROUP = "DEFAULT_GROUP";
	public static final String DEFAULT_SELF = "DEFAULT_SELF";
	public static final String DEFAULT_PER_LIST = "DEFAULT_PER_LIST";
	public static final String DEFAULT_NEXT_PER_LIST="DEFAULT_NEXT_PER_LIST";

    protected int pageSize=20;
    @ModelAttribute
    public void init(HttpServletRequest request, HttpServletResponse response,BasePagination<T> pagination) {
    	Map<String, String[]> map = request.getParameterMap();
        if (map.get("callback") != null) {//对非正式跨域传输协议 callback定义方法进行记录
            jsonpCallback = String.valueOf(((Object[]) map.get("callback"))[0]);
        }
    	currentRequest.set(request);
        currentResponse.set(response);
      
    }
    public static HttpServletRequest request() {
        return (HttpServletRequest) currentRequest.get();
    }
    protected HttpServletResponse response() {
        return (HttpServletResponse) currentResponse.get();
    }
    protected BasePagination<T> getPagination(){
		return currentPagination.get();
	}
	protected String getSuffix(String suffix){
		return "admin/"+suffix;
	}
	protected DwzAjaxResult ajaxDone(int statusCode, String message, String forwardUrl,String callbackType) {
		DwzAjaxResult mav = new DwzAjaxResult();
		mav.setStatusCode(statusCode);
		mav.setMessage(message);
		mav.setForwardUrl(forwardUrl);
		mav.setCallbackType(callbackType);
		return mav;
	}
	/**
     * 向前台原样输出json字符串  为了统一方便 返回方式固定适应多平台调用  （不使用springmvc jsonmodel是为了多元化方便）
     *
     * @return str 需要输出的内容
     */
	protected void toClient(String str) {
		PrintWriter out = null;
		response().setHeader("Content-Type", "text/plain;charset=UTF-8");
		try {
			out = response().getWriter();
			if (StringUtils.noEmpty(jsonpCallback)) {// 对非正式跨域传输协议支持
				out.print(jsonpCallback + "(" + str + ")");
			} else {
				out.print(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.flush();
				out.close();
				out = null;
			}
		}

	}

	protected DwzAjaxResult ajaxDoneSuccessClose(String message) {
		return ajaxDone(200, message, "","closeCurrent");
	}
	protected DwzAjaxResult ajaxDoneExistClose(String message) {
		return ajaxDone(201, message, "","closeCurrent");
	}
	protected DwzAjaxResult ajaxDoneSuccessRefresh(String message) {
		return ajaxDone(200, message, "","forward");
	}
	protected DwzAjaxResult ajaxDoneSuccess(String message) {
		return ajaxDone(200, message, "","");
	}
	protected DwzAjaxResult ajaxDoneErrorClose(String message) {
		return ajaxDone(300, message, "","closeCurrent");
	}
	protected DwzAjaxResult ajaxDoneErrorRefresh(String message) {
		return ajaxDone(300, message, "","forward");
	}
	protected DwzAjaxResult ajaxDoneError(String message) {
		return ajaxDone(300, message, "","");
	}
	protected String getMessage(String code) {
		return this.getMessage(code, new Object[] {});
	}
	protected String getMessage(String code, Object arg0) {
		return this.getMessage(code, new Object[] { arg0 });
	}
	public DwzAjaxResult exception(Exception e, HttpServletRequest request) {
		e.printStackTrace();
		request.setAttribute("exception", e);
		if (AjaxUtils.isAjax(request) || request.getParameter("ajax") != null) {
			return ajaxDoneError(e.getMessage());
		}
		return ajaxDoneError("操作失败");
	}
	protected String getMessage(String code, Object[] args) {
		try {
			return SpringContextUtil.getApplicationContext().getMessage(code, args, Locale.SIMPLIFIED_CHINESE);
		} catch (NoSuchMessageException e) {
			e.printStackTrace();
		}
		return code;
	}
}
