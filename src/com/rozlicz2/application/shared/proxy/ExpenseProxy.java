package com.rozlicz2.application.shared.proxy;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.rozlicz2.application.server.locator.ObjectifyLocator;
import com.rozlicz2.application.shared.entity.Expense;
import com.rozlicz2.application.shared.entity.Expense.PaymentOption;

@ProxyFor(value = Expense.class, locator = ObjectifyLocator.class)
public interface ExpenseProxy extends EntityProxy {
	public List<ExpenseConsumerEntityProxy> getConsumers();

	@Size(max = 32, min = 32, message = "{custom.internal.error}")
	String getId();

	@NotNull(message = "{custom.internal.error}")
	@Size(min = 4, max = 100, message = "{custom.name.size.message}")
	String getName();

	public PaymentOption getPaymentOption();

	public List<ExpensePaymentEntityProxy> getPayments();

	public String getProjectId();

	public Double getSum();

	public void setConsumers(List<ExpenseConsumerEntityProxy> consumers);

	void setId(String id);

	void setName(String name);

	public void setPaymentOption(PaymentOption paymentOption);

	public void setPayments(List<ExpensePaymentEntityProxy> payments);

	public void setProjectId(String projectId);
}
