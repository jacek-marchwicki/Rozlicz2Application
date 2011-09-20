package com.rozlicz2.application.shared.proxy;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.rozlicz2.application.server.locator.ObjectifyLocator;
import com.rozlicz2.application.shared.entity.ProjectList;

@ProxyFor(value = ProjectList.class, locator = ObjectifyLocator.class)
public interface ProjectListProxy extends EntityProxy {
	public String getId();

	public String getName();
}