package com.rozlicz2.application.client.event;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.rozlicz2.application.shared.proxy.ExpenseProxy;

public class ExpensesChangedEvent extends
		GwtEvent<ExpensesChangedEvent.Handler> {
	public interface Handler extends EventHandler {

		void onChended(ExpensesChangedEvent expenseChangedEvent);

	}

	public static final Type<Handler> TYPE = new Type<ExpensesChangedEvent.Handler>();
	private List<ExpenseProxy> expenses;
	private String projectId;

	public ExpensesChangedEvent(String projectId, List<ExpenseProxy> expenses) {
		this.projectId = projectId;
		this.expenses = expenses;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onChended(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	public List<ExpenseProxy> getExpenses() {
		return this.expenses;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setExpenses(List<ExpenseProxy> expenses) {
		this.expenses = expenses;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}
