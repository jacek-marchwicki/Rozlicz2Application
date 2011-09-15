package com.rozlicz2.application.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.rozlicz2.application.client.entity.ExpenseConsumerEntity;
import com.rozlicz2.application.client.entity.ExpensePaymentEntity;
import com.rozlicz2.application.client.entity.IdMap;

public interface ExpenseView extends IsWidget {

	public interface Presenter {
		void addParticipants();

		void consumerSet(Long userId, boolean isConsumer);

		void consumerSetProportional(Long userId, boolean isProportional);

		void consumerSetValue(Long userId, double value);

		void paymentSetValue(Long userId, double value);

		void setExpenseName(String value);
	}

	public void setConsumers(IdMap<ExpenseConsumerEntity> consumers);

	public void setExpenseName(String name);

	public void setPayments(IdMap<ExpensePaymentEntity> payments);

	public void setPaymentsSum(double value);

	public void setPresenter(Presenter presenter);
}
