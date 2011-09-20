package com.rozlicz2.application.shared.proxy;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.EntityProxy;
import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.rozlicz2.application.server.locator.ObjectifyLocator;
import com.rozlicz2.application.shared.entity.Expense;

@ProxyFor(value = Expense.class, locator = ObjectifyLocator.class)
public interface ExpenseProxy extends EntityProxy {
	public List<ExpenseConsumerEntityProxy> getConsumers();

	String getId();

	String getName();

	public List<ExpensePaymentEntityProxy> getPayments();

	public String getProjectId();

	public void setConsumers(List<ExpenseConsumerEntityProxy> consumers);

	void setId(String id);

	void setName(String name);

	public void setPayments(List<ExpensePaymentEntityProxy> payments);

	public void setProjectId(String projectId);
}
