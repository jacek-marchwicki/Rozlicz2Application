package com.rozlicz2.application.shared.entity;


public class AppUser extends DatastoreObject {
	private String email;
	private String facebookId;
	private String googleId;
	private String nickname;

	public AppUser() {

	}

	public String getEmail() {
		return email;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public String getGoogleId() {
		return googleId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

}
