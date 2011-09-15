package com.rozlicz2.application.server.service;

import com.googlecode.objectify.ObjectifyService;
import com.rozlicz2.application.shared.entity.AppUserSession;

public class AppUserSessionDao extends ObjectifyDao<AppUserSession> {
	static {
		ObjectifyService.register(AppUserSession.class);
	}
}
