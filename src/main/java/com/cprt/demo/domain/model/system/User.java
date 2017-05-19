package com.cprt.demo.domain.model.system;

import java.io.Serializable;
import java.util.Date;

import com.cprt.demo.utils.pagination.PagerModel;

public class User extends PagerModel<User> implements Serializable {
	private static final long serialVersionUID = 4110895449921696198L;
	private String userName;
	private String password;
	private Date createTime;
	private Date lastLoginTime;
	private boolean status;

	public User() {
	}
	
	public User(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
