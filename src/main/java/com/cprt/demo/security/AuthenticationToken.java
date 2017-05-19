package com.cprt.demo.security;

import org.apache.shiro.authc.UsernamePasswordToken;


public class AuthenticationToken extends UsernamePasswordToken {

	private static final long serialVersionUID = 5898441540965086534L;


	/**
	 * @param username
	 * @param password
	 * @param rememberMe
	 * @param host
	 */
	public AuthenticationToken(String username, String password, boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
	}

}