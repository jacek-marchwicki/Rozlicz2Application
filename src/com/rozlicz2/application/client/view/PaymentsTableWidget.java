package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.rozlicz2.application.client.view.ExpenseView.ExpensePayment;

public interface PaymentsTableWidget extends IsWidget {
	public void setPayments(List<ExpensePayment> payments);
	
	public void setPresenter(Presenter presenter);
	
	public interface Presenter {
		void setPayments(List<ExpensePayment> payments);
	}
}