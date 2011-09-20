package com.rozlicz2.application.shared.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.rozlicz2.application.shared.entity.ParticipantEntity;

@ProxyFor(value = ParticipantEntity.class)
public interface ParticipantEntityProxy extends ValueProxy {
	String getId();

	String getName();

	void setId(String id);

	void setName(String name);
}
