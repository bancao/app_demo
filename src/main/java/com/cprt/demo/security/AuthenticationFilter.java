package com.cprt.demo.security;

import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.cprt.demo.utils.redis.IRedisClient;

public class AuthenticationFilter extends FormAuthenticationFilter {

	/** 默认"加密密码"参数名称 */
	private static final String DEFAULT_EN_PASSWORD_PARAM = "enPassword";

	/** "加密密码"参数名称 */
	private String enPasswordParam = DEFAULT_EN_PASSWORD_PARAM;
	
	@Autowired
	@Qualifier("redisClient")
    private IRedisClient redisClient;


	@Override
	protected org.apache.shiro.authc.AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
		String username = getUsername(servletRequest);
		String password = getPassword(servletRequest);
		boolean rememberMe = isRememberMe(servletRequest);
		String host = getHost(servletRequest);
		return new AuthenticationToken(username, password, rememberMe, host);
	}

	@Override
	protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
		String requestType = request.getHeader("X-Requested-With");
		if (requestType != null && requestType.equalsIgnoreCase("XMLHttpRequest")) {
			response.addHeader("loginStatus", "accessDenied");
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return false;
		}
		String req_uri=request.getRequestURI(); 
		String queryString = request.getQueryString();
		String url = req_uri+"?"+queryString;
		String currentLoginUrl = getLoginUrl();
		if(currentLoginUrl.indexOf("?url=")==-1){
			setLoginUrl(currentLoginUrl+"?url="+URLEncoder.encode(url));
		}else{
			String oldUrl = currentLoginUrl.substring(currentLoginUrl.indexOf("?url="),currentLoginUrl.length());
			if(!oldUrl.equals("?url="+URLEncoder.encode(url))){
				currentLoginUrl=currentLoginUrl.replace(oldUrl, "?url="+URLEncoder.encode(url));
				setLoginUrl(currentLoginUrl);
			}
		}
		
		return super.onAccessDenied(request, response);
	}

	@Override
	protected boolean onLoginSuccess(org.apache.shiro.authc.AuthenticationToken token, Subject subject, ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		Session session = subject.getSession();
		String url = WebUtils.getSavedRequest(servletRequest).getRequestUrl();
		Map<Object, Object> attributes = new HashMap<Object, Object>();
		Collection<Object> keys = session.getAttributeKeys();
		for (Object key : keys) {
			attributes.put(key, session.getAttribute(key));
		}
		session.stop();
		session = subject.getSession();
		for (Entry<Object, Object> entry : attributes.entrySet()) {
			session.setAttribute(entry.getKey(), entry.getValue());
		}
		WebUtils.issueRedirect(servletRequest, servletResponse, getSuccessUrl());
		return super.onLoginSuccess(token, subject, servletRequest, servletResponse);
	}
	
	
	
	public static void main(String[] args) {
		String url = "/manage/member/list?leftId=evaluation19";
		String currentLoginUrl ="/index";
		System.out.println(currentLoginUrl.indexOf("?url="));
		if(currentLoginUrl.indexOf("?url=")==-1){
					String oldUrl = "?url=%2Fmanage%2Fmember%2Flist%3FleftId%3Devaluation19";
					if(!oldUrl.equals("?url="+URLEncoder.encode(url))){
						currentLoginUrl.replace(oldUrl, "?url="+URLEncoder.encode(url));
					}
					System.out.println(currentLoginUrl+"?url="+URLEncoder.encode(url));
				}
	}
}