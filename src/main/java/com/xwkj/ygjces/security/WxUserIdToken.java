package com.xwkj.ygjces.security;

import org.apache.shiro.authc.UsernamePasswordToken;

public class WxUserIdToken extends UsernamePasswordToken {
	private String userId;

	public WxUserIdToken(String userId) {
		this.userId = userId;
	}

	public WxUserIdToken(String username, char[] password, String userId) {
		super(username, password);
		this.userId = userId;
	}

	public WxUserIdToken(String username, String password, String userId) {
		super(username, password);
		this.userId = userId;
	}

	public WxUserIdToken(String username, char[] password, String host, String userId) {
		super(username, password, host);
		this.userId = userId;
	}

	public WxUserIdToken(String username, String password, String host, String userId) {
		super(username, password, host);
		this.userId = userId;
	}

	public WxUserIdToken(String username, char[] password, boolean rememberMe, String userId) {
		super(username, password, rememberMe);
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
