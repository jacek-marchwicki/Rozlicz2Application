package com.rozlicz2.application.shared.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.rozlicz2.application.server.locator.ObjectifyLocator;
import com.rozlicz2.application.shared.entity.Contact;

@ProxyFor(value = Contact.class, locator = ObjectifyLocator.class)
public interface ContactProxy extends EntityProxy {
	String getId();

	String getName();
}
