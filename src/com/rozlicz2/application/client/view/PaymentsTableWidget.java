package com.rozlicz2.application.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.rozlicz2.application.client.entity.ExpensePaymentEntity;
import com.rozlicz2.application.client.entity.IdMap;

public interface PaymentsTableWidget extends IsWidget {
	public interface Presenter {
		void setPaymentValue(Long userId, double value);
	}

	public void setPayments(IdMap<ExpensePaymentEntity> payments);

	public void setPresenter(Presenter presenter);

	public void setSum(double value);
}