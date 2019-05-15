package com.framework.common;
/**
* @ClassName: AjaxResult
* @Description: (ajax返回对象)
* @author Meiqq
* @date 2017年12月23日 下午5:10:16
* 
*/

public class AjaxResult {
	/**状态编码*/
	private int statusCode;
	/**返回信息*/
    private String message;
    private String callbackType;  
      
    public AjaxResult() {
		super();
	}

	public AjaxResult(int statusCode,String callbackType) {
		super();
		this.statusCode = statusCode;
		this.callbackType = callbackType;
	}
    
	public AjaxResult(int statusCode, String message,String callbackType) {  
        this.statusCode = statusCode;  
        this.message = message;  
        this.callbackType = callbackType;  
    }

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCallbackType() {
		return callbackType;
	}

	public void setCallbackType(String callbackType) {
		this.callbackType = callbackType;
	}
}
