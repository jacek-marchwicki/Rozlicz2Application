package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.rozlicz2.application.shared.entity.Expense.PaymentOption;
import com.rozlicz2.application.shared.proxy.ExpenseConsumerEntityProxy;
import com.rozlicz2.application.shared.proxy.ExpensePaymentEntityProxy;
import com.rozlicz2.application.shared.proxy.ExpenseProxy;

public class ExpenseViewImpl extends Composite implements ExpenseView,
		Editor<ExpenseProxy> {

	interface Driver extends
			SimpleBeanEditorDriver<ExpenseProxy, ExpenseViewImpl> {
	}

	interface ExpenseViewImplUiBinder extends UiBinder<Widget, ExpenseViewImpl> {
	}

	private static ExpenseViewImplUiBinder uiBinder = GWT
			.create(ExpenseViewImplUiBinder.class);

	@UiField
	ConsumersTableWidget consumersEditor;

	Driver driver = GWT.create(Driver.class);

	@UiField
	HTMLPanel htmlPanel;

	@UiField
	EditableLabelWidget nameEditor;

	@UiField
	PaymentOptionWidget paymentOptionEditor;

	@UiField
	PaymentsTableWidget paymentsEditor;

	private Presenter presenter;

	public ExpenseViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		driver.initialize(this);
	}

	@Override
	public SimpleBeanEditorDriver<ExpenseProxy, ?> getDriver() {
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
	public void onNameChange(ValueChangeEvent<String> e) {
		presenter.save();
	}

	@UiHandler("paymentOptionEditor")
	public void onPaymentOptionChange(ValueChangeEvent<PaymentOption> e) {
		presenter.save();
	}

	@UiHandler("paymentsEditor")
	public void onPaymentsChange(
			ValueChangeEvent<List<ExpensePaymentEntityProxy>> e) {
		presenter.save();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}
