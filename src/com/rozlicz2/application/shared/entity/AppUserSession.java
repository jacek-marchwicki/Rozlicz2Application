package com.rozlicz2.application.shared.entity;

import javax.persistence.Id;

import com.googlecode.objectify.Key;

public class AppUserSession {
	@Id
	private String sessionId;
	private Key<AppUser> userKey;

	public String getSessionId() {
		return sessionId;
	}

	public Key<AppUser> getUserKey() {
		return userKey;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setUserKey(AppUser user) {
		userKey = new Key<AppUser>(AppUser.class, user.getId());
	}

	public void setUserKey(Key<AppUser> userKey) {
		this.userKey = userKey;
	}
}
