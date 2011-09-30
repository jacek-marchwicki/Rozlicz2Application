package com.rozlicz2.application.shared.proxy;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.rozlicz2.application.shared.entity.ExpensePaymentEntity;

@ProxyFor(value = ExpensePaymentEntity.class)
public interface ExpensePaymentEntityProxy extends ValueProxy {

	@Size(max = 32, min = 32, message = "{custom.internal.error}")
	String getId();

	@Size(max = 4, min = 100, message = "{custom.internal.error}")
	String getName();

	@Min(value = 0, message = "{custom.expense.payment.not.null}")
	Double getValue();

	void setValue(Double value);
}
