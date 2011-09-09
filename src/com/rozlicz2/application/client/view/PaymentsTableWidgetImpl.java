package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.rozlicz2.application.client.view.ExpenseView.ExpensePayment;

public class PaymentsTableWidgetImpl extends Composite implements
		PaymentsTableWidget {

	interface PaymentsTableWidgetImplUiBinder extends
			UiBinder<Widget, PaymentsTableWidgetImpl> {
	}

	private static PaymentsTableWidgetImplUiBinder uiBinder = GWT
			.create(PaymentsTableWidgetImplUiBinder.class);

	private Presenter presenter;

	@UiField
	Label sumLabel;
	@UiField
	FlexTable table;

	public PaymentsTableWidgetImpl() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	private void initializePaymentsTable() {
		table.clear();
		table.setText(0, 0, "name");
		table.setText(0, 1, "value");
	}

	@Override
	public void setPayments(List<ExpensePayment> payments) {
		initializePaymentsTable();

		int row = 0;

		for (final ExpensePayment expensePayment : payments) {
			row++;
			table.setText(row, 0, expensePayment.name);

			ValueBoxWidgetImpl value = new ValueBoxWidgetImpl();
			value.addValueChangeHandler(new ValueChangeHandler<Double>() {

				@Override
				public void onValueChange(ValueChangeEvent<Double> event) {
					presenter.setPaymentValue(expensePayment.userId,
							event.getValue());

				}
			});
			value.setValue(expensePayment.value);
			table.setWidget(row, 1, value);

		}
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setSum(double value) {
		String formattedValue = NumberFormat.getCurrencyFormat().format(value);
		sumLabel.setText(formattedValue);
	}
}
