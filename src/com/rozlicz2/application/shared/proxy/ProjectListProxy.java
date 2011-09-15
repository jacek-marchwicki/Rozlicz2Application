package com.rozlicz2.application.shared.proxy;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.rozlicz2.application.server.locator.ObjectifyLocator;
import com.rozlicz2.application.shared.entity.ProjectList;

@ProxyFor(value = ProjectList.class, locator = ObjectifyLocator.class)
public interface ProjectListProxy extends EntityProxy {
	public List<ListItemProxy> getItems();

	public String getName();

	public void setItems(List<ListItemProxy> items);

	public void setName(String name);
}