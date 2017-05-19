package com.cprt.demo.web.controller;

import java.net.URLDecoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cprt.demo.domain.model.system.User;
import com.cprt.demo.security.AuthenticationToken;
import com.cprt.demo.service.UserService;
import com.cprt.demo.utils.security.MD5;
import com.cprt.demo.web.model.LoginUser;
import com.google.common.collect.Maps;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
	@Autowired
	UserService userService;
	
	@RequestMapping("/test")
	@ResponseBody
	@RequiresPermissions("admin:user")
	public Map<String, String> test() throws Exception {
		Map<String, String> result = Maps.newHashMap();
		result.put("hi", "ha");
		return result;
	}
	
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	@ResponseBody
	public String login(HttpServletRequest request, HttpServletResponse response, LoginUser loginUser) throws Exception {
		String url =request.getParameter("url");
		User user = userService.login(new User(loginUser.getUserName(), MD5.md5(loginUser.getPassword())));
		if (user != null) {
			AuthenticationToken token = new AuthenticationToken(loginUser.getUserName(), MD5.md5(loginUser.getPassword()), false, request.getRemoteHost());
			Subject subject = SecurityUtils.getSubject();
			subject.getSession().setTimeout(-1000l);
			if (subject != null) {
				subject.login(token);
			}
			if(StringUtils.isNotEmpty(url)){
				request.getSession().setAttribute("forword", URLDecoder.decode(url));
			}
		} else {
			response.sendRedirect("/login/error");
		}
		
		return "/index.jsp";
	}
	
	@RequestMapping(value="/app/login", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> appLogin(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginUser loginUser) throws Exception {
		Map<String, String> result = Maps.newHashMap();
		try {
			AuthenticationToken token = new AuthenticationToken(loginUser.getUserName(), MD5.md5(loginUser.getPassword()), false, request.getRemoteHost());
			Subject subject = SecurityUtils.getSubject();
			if (subject != null) {
				subject.login(token);
			}
			return result;
		} catch (Exception e) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			result.put("error", "unauthorized");
			return result;
		}
	}
	
	@RequestMapping(value="/app/logout", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, String> appLogout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, String> result = Maps.newHashMap();
		try {
			Subject subject = SecurityUtils.getSubject();
			if (subject != null) {
				subject.logout();
			}
			return result;
		} catch (Exception e) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			result.put("error", "unauthorized");
			return result;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(MD5.md5("test"));
	}
}
