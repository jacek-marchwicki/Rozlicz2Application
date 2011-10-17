package com.rozlicz2.application.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.rozlicz2.application.shared.proxy.ExpenseProxy;

public class ExpenseChangedEvent extends GwtEvent<ExpenseChangedEvent.Handler> {

	public interface Handler extends EventHandler {
		void onExpenseChange(ExpenseChangedEvent expenseChangedEvent);
	}

	public final static Type<Handler> TYPE = new Type<ExpenseChangedEvent.Handler>();
	private final ExpenseProxy readOnlyExpense;

	public ExpenseChangedEvent(ExpenseProxy readOnlyExpense) {
		this.readOnlyExpense = readOnlyExpense;
	}

	@Override
	protected void dispatch(Handler handler) {
		handler.onExpenseChange(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<Handler> getAssociatedType() {
		return TYPE;
	}

	public ExpenseProxy getReadOnlyExpense() {
		return readOnlyExpense;
	}
}
