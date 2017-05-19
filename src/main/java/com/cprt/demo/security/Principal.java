
package com.cprt.demo.security;

import java.io.Serializable;


public class Principal implements Serializable {


	private static final long serialVersionUID = -2607760637362349876L;

	private Integer id;

	private String loginAccount;
	
	private Integer roleId;

	/**
	 * 
	 * @param id
	 * @param loginAccount
	 * @param roleId
	 */
	public Principal(Integer id, String loginAccount, Integer roleId) {
		this.id = id;
		this.loginAccount = loginAccount;
		this.roleId = roleId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLoginAccount() {
		return loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}

	@Override
	public String toString(){
		return loginAccount;
	}
	
	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

}