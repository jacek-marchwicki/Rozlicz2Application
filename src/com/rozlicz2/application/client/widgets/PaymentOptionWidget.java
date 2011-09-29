package com.rozlicz2.application.client.widgets;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.rozlicz2.application.client.resources.ApplicationConstants;
import com.rozlicz2.application.shared.entity.Expense;
import com.rozlicz2.application.shared.entity.Expense.PaymentOption;

public class PaymentOptionWidget extends VerticalPanel implements
		LeafValueEditor<Expense.PaymentOption>,
		HasValueChangeHandlers<Expense.PaymentOption>, IsWidget {
	private final RadioButton radioButtonEverybody;
	private final RadioButton radioButtonOnlyMe;
	private final RadioButton radioButtonSelectedUsers;

	public PaymentOptionWidget() {
		radioButtonEverybody = new RadioButton("participant",
				ApplicationConstants.constants.everybody());
		radioButtonEverybody.setValue(true);
		radioButtonOnlyMe = new RadioButton("participant",
				ApplicationConstants.constants.onlyMe());
		radioButtonSelectedUsers = new RadioButton("participant",
				ApplicationConstants.constants.selectedUsers());
		add(radioButtonEverybody);
		add(radioButtonOnlyMe);
		add(radioButtonSelectedUsers);
		ValueChangeHandler<Boolean> valueChangeHandler = new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				PaymentOption value = getValue();
				ValueChangeEvent.fire(PaymentOptionWidget.this, value);
			}
		};
		radioButtonEverybody.addValueChangeHandler(valueChangeHandler);
		radioButtonOnlyMe.addValueChangeHandler(valueChangeHandler);
		radioButtonSelectedUsers.addValueChangeHandler(valueChangeHandler);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<PaymentOption> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public PaymentOption getValue() {
		if (radioButtonEverybody.getValue())
			return PaymentOption.EVERYBODY;
		if (radioButtonOnlyMe.getValue())
			return PaymentOption.ONLY_ME;
		else
			return PaymentOption.SELECTED_USERS;
	}

	@Override
	public void setValue(PaymentOption value) {
		radioButtonEverybody.setValue(value.equals(PaymentOption.EVERYBODY));
		radioButtonOnlyMe.setValue(value.equals(PaymentOption.ONLY_ME));
		radioButtonSelectedUsers.setValue(value
				.equals(PaymentOption.SELECTED_USERS));
	}
}
