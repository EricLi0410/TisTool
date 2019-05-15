package com.framework.common;

public class DwzAjaxResult {
	private int statusCode;  
    private String message;  
    private String navTabId;  
    private String callbackType;  
    private String forwardUrl;  
    private String rel;
      
    public DwzAjaxResult() {
		super();
	}

	public DwzAjaxResult(int statusCode,String callbackType) {
		super();
		this.statusCode = statusCode;
		this.callbackType = callbackType;
	}
    
	public DwzAjaxResult(int statusCode, String message, String rel) {
		super();
		this.statusCode = statusCode;
		this.message = message;
		this.rel = rel;
	}

	public DwzAjaxResult(int statusCode, String message, String navTabId,  
            String callbackType, String forwardUrl) {  
        this.statusCode = statusCode;  
        this.message = message;  
        this.navTabId = navTabId;  
        this.callbackType = callbackType;  
        this.forwardUrl = forwardUrl;  
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

	public String getNavTabId() {
		return navTabId;
	}

	public void setNavTabId(String navTabId) {
		this.navTabId = navTabId;
	}

	public String getCallbackType() {
		return callbackType;
	}

	public void setCallbackType(String callbackType) {
		this.callbackType = callbackType;
	}

	public String getForwardUrl() {
		return forwardUrl;
	}

	public void setForwardUrl(String forwardUrl) {
		this.forwardUrl = forwardUrl;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}
    
}
