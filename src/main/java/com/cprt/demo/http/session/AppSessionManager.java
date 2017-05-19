package com.cprt.demo.http.session;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import com.cprt.demo.http.cookie.CookieUtil;
import com.google.common.collect.Maps;

public class AppSessionManager extends DefaultWebSessionManager {

	public static final Map<String, Serializable> MAP = Maps.newHashMap();
	private static Integer SESSION_COOKIE_TIME = 30 * 60;// sessionId的cookie存活时间。单位为S
	private static Integer SESSION_TIME = 30 * 60 * 1000;// session的过期时间B/S客户端。单位为MS
	private static final String SEESION_NAME = "JSESSIONID";
	private static final String ACCESS_TOKEN = "access_token";
	
	/**
	 * 根据客户端的sessionIdKey获取真正的sessionId
	 */
	@Override
	protected Serializable getSessionId(ServletRequest request,
			ServletResponse response) {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String sessionId = mygetSeesionId(req);
		Serializable id = MAP.get(sessionId);
		if (null != sessionId && !sessionId.isEmpty()) {
			// 延长cookie过期时间
			mysetSessionIdKeyCookie(res, sessionId, SESSION_COOKIE_TIME);
		}
		return id;
	}

	/**
	 *创建一个session
	 */
	@Override
	protected void onStart(Session session, SessionContext context) {
		// 判断是否是http请求
		if (!WebUtils.isHttp(context)) {
//			log.debug("HTTP请求才能创建session");
			return;
		}
		HttpServletRequest request = WebUtils.getHttpRequest(context);
		HttpServletResponse response = WebUtils.getHttpResponse(context);
		request.removeAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE);
		request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_IS_NEW, Boolean.TRUE);
		String sessionId = UUID.randomUUID().toString().trim();
		Serializable id = session.getId();
		mysetSessionIdKeyCookie(response, sessionId, SESSION_COOKIE_TIME);//设置cookie过期时间
		session.setTimeout(SESSION_TIME);// 设置C/S的session过期时间
		MAP.put(sessionId, id);// 存储sessionIdKey和真正的sessionId
	}

	/**
	 * 获取客户端存储的sessionIdKey
	 * @param request
	 * @return
	 */
	private String mygetSeesionId(HttpServletRequest request) {
		String sessionId = null;
		try {
			sessionId = request.getHeader(ACCESS_TOKEN);
			if (StringUtils.isBlank(sessionId)) {
				sessionId = CookieUtil.getCookieValue(request, SEESION_NAME);
			}
		} catch (Exception e) {
//			log.debug("获取sessionId失败");
		}
		return sessionId;
	}

	/**
	 * 设置sessionIdKey的cookie
	 * 
	 * @param sessionId
	 *            sessionId
	 * @param age
	 *            age
	 */
	private void mysetSessionIdKeyCookie(HttpServletResponse response,
			String sessionId, Integer age) {
		Cookie cookie = new Cookie(SEESION_NAME, sessionId);
		cookie.setHttpOnly(Boolean.TRUE);
		cookie.setPath("/");
		cookie.setMaxAge(SESSION_COOKIE_TIME);
		response.addCookie(cookie);
		response.addHeader(ACCESS_TOKEN, sessionId);
	}
}
