package com.rozlicz2.application.shared.proxy;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.rozlicz2.application.server.locator.ObjectifyLocator;
import com.rozlicz2.application.shared.entity.ProjectList;

@ProxyFor(value = ProjectList.class, locator = ObjectifyLocator.class)
public interface ProjectListProxy extends EntityProxy {

	@Size(max = 32, min = 32, message = "{custom.internal.error}")
	public String getId();

	@NotNull(message = "{custom.internal.error}")
	@Size(min = 4, max = 100, message = "{custom.name.size.message}")
	public String getName();

	@NotNull(message = "{custom.internal.error}")
	public Double getSum();
}