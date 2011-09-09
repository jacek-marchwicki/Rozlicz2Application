package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.rozlicz2.application.client.view.ExpenseView.ExpensePayment;

public interface PaymentsTableWidget extends IsWidget {
	public interface Presenter {
		void setPayments(List<ExpensePayment> payments);

		void setPaymentValue(Long userId, double value);
	}

	public void setPayments(List<ExpensePayment> payments);

	public void setPresenter(Presenter presenter);

	public void setSum(double value);
}