package com.rozlicz2.application.server.service;

import java.util.List;

import com.googlecode.objectify.ObjectifyService;
import com.rozlicz2.application.shared.entity.Contact;

public class ContactDao extends ObjectifyDao<Contact> {
	static {
		ObjectifyService.register(Contact.class);
	}

	@Override
	public List<Contact> listAll() {
		return listAllForUser();
	}
}
