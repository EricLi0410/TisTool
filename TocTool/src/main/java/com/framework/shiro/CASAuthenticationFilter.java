package com.framework.shiro;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.DefaultGatewayResolverImpl;
import org.jasig.cas.client.authentication.GatewayResolver;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.validation.Assertion;


/**
* @ClassName: CASAuthenticationFilter
* @Description: (cas自定义拦截器)
* @author Meiqq
* @date 2017年12月23日 下午5:39:17
* 
*/

public class CASAuthenticationFilter extends AbstractCasFilter{
	 /** 
     * The URL to the CAS Server login. 
     */  
    public static String casServerLoginUrl;  
  
    /** 
     * Whether to send the renew request or not. 
     */  
    private boolean renew = false;  
  
    /** 
     * Whether to send the gateway request or not. 
     */  
    private boolean gateway = false;  
    /** 
     * 添加属性，这里用来存放不过滤地址正则表达式，可以根据自己需求定制---1 
     */  
    private String excludePaths;  
      
    private GatewayResolver gatewayStorage = new DefaultGatewayResolverImpl();  
  
    protected void initInternal(final FilterConfig filterConfig) throws ServletException {  
        if (!isIgnoreInitConfiguration()) {  
            super.initInternal(filterConfig);  
            setCasServerLoginUrl(getPropertyFromInitParams(filterConfig, "casServerLoginUrl", null));  
            log.trace("Loaded CasServerLoginUrl parameter: " + casServerLoginUrl);  
            setRenew(parseBoolean(getPropertyFromInitParams(filterConfig, "renew", "true")));  
            log.trace("Loaded renew parameter: " + this.renew);  
            setGateway(parseBoolean(getPropertyFromInitParams(filterConfig, "gateway", "false")));  
            log.trace("Loaded gateway parameter: " + this.gateway);  
  
            final String gatewayStorageClass = getPropertyFromInitParams(filterConfig, "gatewayStorageClass", null);  
  
            if (gatewayStorageClass != null) {  
                try {  
                    this.gatewayStorage = (GatewayResolver) Class.forName(gatewayStorageClass).newInstance();  
                } catch (final Exception e) {  
                    log.error(e,e);  
                    throw new ServletException(e);  
                }  
            }  
            //用来读取web配置文件中excludes属性值  
            excludePaths = getPropertyFromInitParams(filterConfig, "excludePaths", null);//filterConfig.getInitParameter("excludePaths");  
            excludePaths = excludePaths.trim();  
        }  
    }  
  
    public void init() {  
        super.init();  
        CommonUtils.assertNotNull(casServerLoginUrl, "casServerLoginUrl cannot be null.");  
    }  
// 	url判断逻辑
    private boolean isExclude(String uri){
        boolean isInWhiteList = false;
        if(uri.startsWith("/js") || uri.startsWith("/css") || uri.startsWith("/img") || uri.startsWith("/images") || uri.startsWith("/plugins") || uri.startsWith("/fonts") || uri.startsWith("/video") || uri.equals("/index.jsp") || uri.startsWith("/attachment"))
        	return true;
        if(excludePaths!=null && uri!=null){
        	String[] _excludePath = excludePaths.trim().split(",");
        	for (String string : _excludePath) {
        		isInWhiteList = uri.matches(string);
        		if(isInWhiteList)
        			return isInWhiteList;
			}
        }
        return isInWhiteList;  
    }  
     
      
    public final void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {  
        final HttpServletRequest request = (HttpServletRequest) servletRequest;  
        final HttpServletResponse response = (HttpServletResponse) servletResponse;  
        final HttpSession session = request.getSession(false);  
        final Assertion assertion = session != null ? (Assertion) session.getAttribute(CONST_CAS_ASSERTION) : null;  
        if(isExclude(request.getServletPath())){  
            filterChain.doFilter(request, response);  
            return;  
        }  
        if (assertion != null) {  
            filterChain.doFilter(request, response);  
            return;  
        }  
  
        final String serviceUrl = constructServiceUrl(request, response);  
        final String ticket = CommonUtils.safeGetParameter(request,getArtifactParameterName());  
        final boolean wasGatewayed = this.gatewayStorage.hasGatewayedAlready(request, serviceUrl);  
  
        if (CommonUtils.isNotBlank(ticket) || wasGatewayed) {  
            filterChain.doFilter(request, response);  
            return;  
        }  
  
        final String modifiedServiceUrl;  
  
        log.debug("no ticket and no assertion found");  
        if (this.gateway) {  
            log.debug("setting gateway attribute in session");  
            modifiedServiceUrl = this.gatewayStorage.storeGatewayInformation(request, serviceUrl);  
        } else {  
            modifiedServiceUrl = serviceUrl;  
        }  
  
        if (log.isDebugEnabled()) {  
            log.debug("Constructed service url: " + modifiedServiceUrl);  
        }  
  
        final String urlToRedirectTo = CommonUtils.constructRedirectUrl(casServerLoginUrl, getServiceParameterName(), modifiedServiceUrl, this.renew, this.gateway);  
  
        if (log.isDebugEnabled()) {  
            log.debug("redirecting to \"" + urlToRedirectTo + "\"");  
        }  
  
        response.sendRedirect(urlToRedirectTo);  
    }  
  
    
    public final void setRenew(final boolean renew) {  
        this.renew = renew;  
    }  
  
    public final void setGateway(final boolean gateway) {  
        this.gateway = gateway;  
    }  
  
	public final void setCasServerLoginUrl(final String casServerLoginUrl) {  
        CASAuthenticationFilter.casServerLoginUrl = casServerLoginUrl;  
    }  
      
    public final void setGatewayStorage(final GatewayResolver gatewayStorage) {  
        this.gatewayStorage = gatewayStorage;  
    }
}
