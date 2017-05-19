package com.cprt.demo.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alibaba.druid.support.json.JSONUtils;
import com.google.common.collect.Maps;

public abstract class BaseController {
	
	
	@ExceptionHandler({ UnauthenticatedException.class, AuthenticationException.class })
	public String authenticationException(HttpServletRequest request, HttpServletResponse response) {
		if (isAjaxRequest(request)) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			writeJson(Maps.newHashMap(), response);
			return null;
		} else {
			return "redirect:/system/login";
		}
	}

	@ExceptionHandler({ UnauthorizedException.class, AuthorizationException.class })
	public String authorizationException(HttpServletRequest request, HttpServletResponse response) {
		if (isAjaxRequest(request)) {
			response.setStatus(HttpStatus.PRECONDITION_FAILED.value());
			writeJson(Maps.newHashMap(), response);
			return null;
		} else {
			return "redirect:/system/403";
		}
	}

	
	private void writeJson(Map<String, Object> map, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json; charset=utf-8");
			out = response.getWriter();
			out.write(JSONUtils.toJSONString(map));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	public static boolean isAjaxRequest(HttpServletRequest request) {
		return StringUtils.isNotBlank("access_token");
	}

}
