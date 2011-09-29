package com.rozlicz2.application.shared.proxy;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.rozlicz2.application.server.locator.ObjectifyLocator;
import com.rozlicz2.application.shared.entity.ProjectList;

@ProxyFor(value = ProjectList.class, locator = ObjectifyLocator.class)
public interface ProjectListProxy extends EntityProxy {

	@Size(max = 32, min = 32)
	public String getId();

	@NotNull
	@Size(min = 4)
	public String getName();
}