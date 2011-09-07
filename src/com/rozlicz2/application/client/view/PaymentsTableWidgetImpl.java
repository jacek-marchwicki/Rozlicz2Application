package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.rozlicz2.application.client.view.ExpenseView.ExpensePayment;

public class PaymentsTableWidgetImpl extends Composite implements PaymentsTableWidget {

	private static PaymentsTableWidgetImplUiBinder uiBinder = GWT
			.create(PaymentsTableWidgetImplUiBinder.class);

	interface PaymentsTableWidgetImplUiBinder extends
			UiBinder<Widget, PaymentsTableWidgetImpl> {
	}

	public PaymentsTableWidgetImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	FlexTable table;
	private Presenter presenter;

	@Override
	public void setPayments(List<ExpensePayment> payments) {
		initializePaymentsTable();
		int row = 0;
		
		for (ExpensePayment expensePayment : payments) {
			row++;
			table.setText(row, 0, expensePayment.name);
			
			TextBox valueTextBox = new TextBox();
			String formattedValue = NumberFormat.getCurrencyFormat().format(expensePayment.value);
			valueTextBox.setText(formattedValue);
			
			table.setWidget(row, 1, valueTextBox);
		
		}
	}

	private void initializePaymentsTable() {
		table.clear();
		table.setText(0, 0, "name");
		table.setText(0, 1, "value");
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}
