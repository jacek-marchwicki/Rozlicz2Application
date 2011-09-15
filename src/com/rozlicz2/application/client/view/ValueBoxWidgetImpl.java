package com.rozlicz2.application.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ValueBoxWidgetImpl extends Composite implements ValueBoxWidget,
		HasValueChangeHandlers<Double> {

	interface ValueBoxWidgetImplUiBinder extends
			UiBinder<Widget, ValueBoxWidgetImpl> {
	}

	private static ValueBoxWidgetImplUiBinder uiBinder = GWT
			.create(ValueBoxWidgetImplUiBinder.class);

	@UiField
	Label errorLabel;

	@UiField
	TextBox textBox;

	private boolean valid;

	private double value;

	public ValueBoxWidgetImpl() {
		initWidget(uiBinder.createAndBindUi(this));

	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Double> handler) {

		return addHandler(handler, ValueChangeEvent.getType());

	}

	@UiHandler("textBox")
	public void onBlur(BlurEvent e) {

		errorLabel.setText("test test");
		try {
			String text = textBox.getText();
			value = NumberFormat.getDecimalFormat().parse(text);
			errorLabel.setText(null);
			valid = true;

			String formattedValue = NumberFormat.getCurrencyFormat().format(
					value);
			textBox.setText(formattedValue);
			ValueChangeEvent.fire(ValueBoxWidgetImpl.this, new Double(value));

		} catch (NumberFormatException e1) {
			errorLabel.setText("invalid invalid");
			valid = false;

		}

	}

	@UiHandler("textBox")
	void onChange(ValueChangeEvent<String> e) {
		validate();
	}

	@UiHandler("textBox")
	public void onFocus(FocusEvent e) {
		if (!valid)
			return;
		String format = NumberFormat.getDecimalFormat().format(value);
		textBox.setText(format);
	}

	@UiHandler("textBox")
	void onKeyUpEvent(KeyUpEvent e) {
		validate();
	}

	@Override
	public void setValue(double value) {
		this.value = value;
		valid = true;
		String formattedValue = NumberFormat.getCurrencyFormat().format(value);
		textBox.setText(formattedValue);

	}

	private void validate() {
		try {
			String text = textBox.getText();
			NumberFormat.getDecimalFormat().parse(text);
			errorLabel.setText(null);
		} catch (NumberFormatException e) {
			errorLabel.setText("invalid invalid");
		}
	}

}
