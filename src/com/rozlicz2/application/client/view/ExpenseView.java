package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

public interface ExpenseView extends IsWidget {
	public static class ExpensePayment {
		public Long userId;
		public String name;
		public double value;
	}
	public static class ExpenseConsumer {
		public Long userId;
		public String name;
		public boolean isConsumer;
		public boolean isProportional;
		public double value;
	}
	public void setExpenseName(String name);
	public void setPresenter(Presenter presenter);
	public void setPayments(List<ExpensePayment> payments);
	public void setPaymentsSum(double value);
	public void setConsumers(List<ExpenseConsumer> consumers);
	public interface Presenter {
		void paymentSetValue(Long userId, double value);
		void consumerSet(Long userId, boolean isConsumer);
		void consumerSetProportional(Long userId, boolean isProportional);
		void consumerSetValue(Long userId, double value);
		void setExpenseName(String value);
		void addParticipants();
	}
}
