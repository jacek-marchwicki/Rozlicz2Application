package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.rozlicz2.application.client.resources.LocalizedMessages;
import com.rozlicz2.application.client.widgets.ConsumersTableWidget;
import com.rozlicz2.application.client.widgets.EditableTextWidget;
import com.rozlicz2.application.client.widgets.LockWidget;
import com.rozlicz2.application.client.widgets.PaymentOptionWidget;
import com.rozlicz2.application.client.widgets.PaymentsTableWidget;
import com.rozlicz2.application.shared.entity.Expense.PaymentOption;
import com.rozlicz2.application.shared.proxy.ExpenseConsumerEntityProxy;
import com.rozlicz2.application.shared.proxy.ExpensePaymentEntityProxy;
import com.rozlicz2.application.shared.proxy.ExpenseProxy;

public class ExpenseViewImpl extends Composite implements ExpenseView,
		Editor<ExpenseProxy> {

	interface Driver extends
			RequestFactoryEditorDriver<ExpenseProxy, ExpenseViewImpl> {
	}

	interface ExpenseViewImplUiBinder extends UiBinder<Widget, ExpenseViewImpl> {
	}

	private static ExpenseViewImplUiBinder uiBinder = GWT
			.create(ExpenseViewImplUiBinder.class);

	@UiField
	ConsumersTableWidget consumersEditor;
	Driver driver = GWT.create(Driver.class);

	@UiField
	LockWidget lockWidget;

	@UiField
	EditableTextWidget nameEditor;

	@UiField
	PaymentOptionWidget paymentOptionEditor;

	@UiField
	PaymentsTableWidget paymentsEditor;

	private Presenter presenter;

	@UiField
	@Editor.Ignore
	Label sumLabel;

	public ExpenseViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		driver.initialize(this);
	}

	@Override
	public RequestFactoryEditorDriver<ExpenseProxy, ?> getDriver() {
		return driver;
	}

	@UiHandler("addParticipantButton")
	public void onAddParticipant(ClickEvent e) {
		this.presenter.addParticipants();
	}

	@UiHandler("consumersEditor")
	public void onConsumersChange(
			ValueChangeEvent<List<ExpenseConsumerEntityProxy>> e) {
		presenter.save();
	}

	@UiHandler("nameEditor")
	public void onNameEditorChange(ValueChangeEvent<String> e) {
		presenter.validate();
	}

	@UiHandler("paymentOptionEditor")
	public void onPaymentOptionChange(ValueChangeEvent<PaymentOption> e) {
		presenter.save();
	}

	@UiHandler("paymentsEditor")
	public void onPaymentsChange(
			ValueChangeEvent<List<ExpensePaymentEntityProxy>> e) {
		presenter.validate();
	}

	@Override
	public void setLocked(boolean locked) {
		lockWidget.setVisible(locked);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setSum(Double sum) {
		sumLabel.setText(LocalizedMessages.messages.paymentsSum(sum));
	}

}
