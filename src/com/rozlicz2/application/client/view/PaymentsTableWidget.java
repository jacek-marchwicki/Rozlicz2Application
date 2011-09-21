package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.rozlicz2.application.shared.proxy.ExpensePaymentEntityProxy;

public class PaymentsTableWidget extends Composite implements
		LeafValueEditor<List<ExpensePaymentEntityProxy>>,
		HasValueChangeHandlers<List<ExpensePaymentEntityProxy>> {

	interface PaymentsTableWidgetUiBinder extends
			UiBinder<Widget, PaymentsTableWidget> {
	}

	private static PaymentsTableWidgetUiBinder uiBinder = GWT
			.create(PaymentsTableWidgetUiBinder.class);

	@UiField
	FlexTable table;

	private List<ExpensePaymentEntityProxy> values;

	public PaymentsTableWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<List<ExpensePaymentEntityProxy>> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public List<ExpensePaymentEntityProxy> getValue() {
		return values;
	}

	private void initializePaymentsTable() {
		table.clear();
		table.setText(0, 0, "name");
		table.setText(0, 1, "value");
	}

	protected void setPaymentValue(String id, Double currencyValue) {
		for (ExpensePaymentEntityProxy value : values) {
			if (value.getId().equals(id)) {
				value.setValue(currencyValue);
				ValueChangeEvent.fire(this, values);
				return;
			}
		}
		assert false;
	}

	@Override
	public void setValue(List<ExpensePaymentEntityProxy> values) {
		this.values = values;
		initializePaymentsTable();

		int row = 0;

		for (final ExpensePaymentEntityProxy expensePayment : values) {
			row++;
			table.setText(row, 0, expensePayment.getName());

			ValueBoxWidgetImpl value = new ValueBoxWidgetImpl();
			value.addValueChangeHandler(new ValueChangeHandler<Double>() {

				@Override
				public void onValueChange(ValueChangeEvent<Double> event) {
					setPaymentValue(expensePayment.getId(), event.getValue());
				}
			});
			value.setValue(expensePayment.getValue());
			table.setWidget(row, 1, value);

		}
	}
}
