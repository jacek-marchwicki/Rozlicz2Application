package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.rozlicz2.application.shared.proxy.ExpenseConsumerEntityProxy;

public class ConsumersTableWidget extends FlexTable implements IsWidget,
		LeafValueEditor<List<ExpenseConsumerEntityProxy>>,
		HasValueChangeHandlers<List<ExpenseConsumerEntityProxy>> {

	private List<ExpenseConsumerEntityProxy> values;

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<List<ExpenseConsumerEntityProxy>> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public List<ExpenseConsumerEntityProxy> getValue() {
		return values;
	}

	private void initializePaymentsTable() {
		clear();
		setText(0, 0, "name");
		setText(0, 1, "value");
	}

	protected void setConsumerValue(String id, Double currencyValue) {
		for (ExpenseConsumerEntityProxy value : values) {
			if (value.getId().equals(id)) {
				value.setValue(currencyValue);
				ValueChangeEvent.fire(this, values);
				return;
			}
		}
		assert false;
	}

	@Override
	public void setValue(List<ExpenseConsumerEntityProxy> values) {
		this.values = values;
		initializePaymentsTable();

		int row = 0;
		for (final ExpenseConsumerEntityProxy value : values) {
			row++;
			setText(row, 0, value.getName());

			ValueBoxWidgetImpl valueBox = new ValueBoxWidgetImpl();
			valueBox.addValueChangeHandler(new ValueChangeHandler<Double>() {

				@Override
				public void onValueChange(ValueChangeEvent<Double> event) {
					setConsumerValue(value.getId(), event.getValue());
				}
			});
			valueBox.setValue(value.getValue());
			setWidget(row, 1, valueBox);
		}

	}

}
