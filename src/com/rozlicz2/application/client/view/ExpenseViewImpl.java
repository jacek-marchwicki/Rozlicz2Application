package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.rozlicz2.application.shared.proxy.ExpensePaymentEntityProxy;
import com.rozlicz2.application.shared.proxy.ExpenseProxy;

public class ExpenseViewImpl extends Composite implements ExpenseView,
		Editor<ExpenseProxy> {

	interface Driver extends
			SimpleBeanEditorDriver<ExpenseProxy, ExpenseViewImpl> {
	}

	interface ExpenseViewImplUiBinder extends UiBinder<Widget, ExpenseViewImpl> {
	}

	public static class PaymentCell extends
			AbstractCell<ExpensePaymentEntityProxy> {

		interface Template extends SafeHtmlTemplates {
			@SafeHtmlTemplates.Template("<div>{0}</div><div>{1}</div>")
			SafeHtml productCellTemplate(String name, String price);
		}

		private static Template template;

		public PaymentCell() {
			if (template == null)
				template = GWT.create(Template.class);
		}

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				ExpensePaymentEntityProxy value, SafeHtmlBuilder sb) {
			sb.append(template.productCellTemplate(value.getName(),
					"123,43 PLN"));
		}
	}

	private static ExpenseViewImplUiBinder uiBinder = GWT
			.create(ExpenseViewImplUiBinder.class);

	Driver driver = GWT.create(Driver.class);

	@UiField
	HTMLPanel htmlPanel;

	@UiField
	EditableLabelWidget nameEditor;

	@UiField
	PaymentsTableWidget paymentsEditor;

	private Presenter presenter;

	@UiField
	@Editor.Ignore
	RadioButton radioButtonAll;

	@UiField
	@Editor.Ignore
	RadioButton radioButtonMe;

	@UiField
	@Editor.Ignore
	RadioButton radioButtonSelected;

	public ExpenseViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));
		radioButtonAll.setValue(true);
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

	@UiHandler({ "radioButtonSelected", "radioButtonAll", "radioButtonMe" })
	void onChangeParticipants(ValueChangeEvent<Boolean> e) {
		// TODO
	}

	@UiHandler("paymentsEditor")
	public void onExpenseChange(
			ValueChangeEvent<List<ExpensePaymentEntityProxy>> e) {
		presenter.save();
	}

	@UiHandler("nameEditor")
	public void onExpenseNameChange(ValueChangeEvent<String> e) {
		presenter.save();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}
