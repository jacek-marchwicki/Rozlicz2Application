package com.rozlicz2.application.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.rozlicz2.application.shared.proxy.ExpensePaymentEntityProxy;

public class ExpensePaymentEntityWidget extends Composite implements
		ValueAwareEditor<ExpensePaymentEntityProxy>,
		HasValueChangeHandlers<ExpensePaymentEntityProxy> {

	interface ExpensePaymentEntityWidgetUiBinder extends
			UiBinder<Widget, ExpensePaymentEntityWidget> {
	}

	private static ExpensePaymentEntityWidgetUiBinder uiBinder = GWT
			.create(ExpensePaymentEntityWidgetUiBinder.class);

	@UiField
	Label nameEditor;

	private ExpensePaymentEntityProxy value;

	@UiField
	CurrencyWidget valueEditor;

	public ExpensePaymentEntityWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<ExpensePaymentEntityProxy> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public void flush() {
	}

	@Override
	public void onPropertyChange(String... paths) {
	}

	@UiHandler("valueEditor")
	public void onValueEditorChange(ValueChangeEvent<Double> value) {
		ValueChangeEvent.fire(this, this.value);
	}

	@Override
	public void setDelegate(EditorDelegate<ExpensePaymentEntityProxy> delegate) {
	}

	@Override
	public void setValue(ExpensePaymentEntityProxy value) {
		this.value = value;
	}

}
