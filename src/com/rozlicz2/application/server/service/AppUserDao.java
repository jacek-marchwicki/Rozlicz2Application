package com.rozlicz2.application.server.service;

import com.googlecode.objectify.ObjectifyService;
import com.rozlicz2.application.shared.entity.AppUser;

public class AppUserDao extends ObjectifyDao<AppUser> {
	static {
		ObjectifyService.register(AppUser.class);
	}
}
