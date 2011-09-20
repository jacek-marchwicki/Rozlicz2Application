package com.rozlicz2.application.shared.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.rozlicz2.application.shared.entity.ExpenseConsumerEntity;

@ProxyFor(value = ExpenseConsumerEntity.class)
public interface ExpenseConsumerEntityProxy extends ValueProxy {
	String getId();

	String getName();

	Double getValue();

	void setValue(Double value);
}
