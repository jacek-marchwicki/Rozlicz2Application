package com.rozlicz2.application.shared.proxy;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.rozlicz2.application.shared.entity.ExpenseConsumerEntity;

@ProxyFor(value = ExpenseConsumerEntity.class)
public interface ExpenseConsumerEntityProxy extends ValueProxy {
	String getId();

	public Boolean getIsConsumer();

	public Boolean getIsProportional();

	String getName();

	Double getValue();

	void setConsumer(Boolean isConsumer);

	void setProportional(Boolean isProportional);

	void setValue(Double value);
}
