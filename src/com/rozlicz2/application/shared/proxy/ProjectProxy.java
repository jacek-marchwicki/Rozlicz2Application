package com.rozlicz2.application.shared.proxy;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.rozlicz2.application.server.locator.ObjectifyLocator;
import com.rozlicz2.application.shared.entity.Project;

@ProxyFor(value = Project.class, locator = ObjectifyLocator.class)
public interface ProjectProxy extends ProjectListProxy {

	public List<ParticipantEntityProxy> getParticipants();

	public void setId(String name);

	public void setName(String name);

	public void setParticipants(List<ParticipantEntityProxy> participants);
}
