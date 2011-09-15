package com.rozlicz2.application.client.view;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.rozlicz2.application.client.entity.BaseEntity;
import com.rozlicz2.application.client.entity.ExpenseConsumerEntity;
import com.rozlicz2.application.client.entity.ExpensePaymentEntity;
import com.rozlicz2.application.client.entity.IdMap;
import com.rozlicz2.application.client.resources.ApplicationConstants;

public class ExpenseViewImpl extends Composite implements ExpenseView,
		com.rozlicz2.application.client.view.PaymentsTableWidget.Presenter {

	public static class ConsumerCell extends
			AbstractCell<ExpenseConsumerEntity> {

		interface Template extends SafeHtmlTemplates {
			@SafeHtmlTemplates.Template("<div>{0}</div><div>{1}</div>")
			SafeHtml productCellTemplate(String name, String price);
		}

		private static Template template;

		public ConsumerCell() {
			if (template == null)
				template = GWT.create(Template.class);
		}

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				ExpenseConsumerEntity value, SafeHtmlBuilder sb) {
			sb.append(template.productCellTemplate(value.getName(),
					"123,45 PLN"));
		}
	}

	interface ExpenseViewImplUiBinder extends UiBinder<Widget, ExpenseViewImpl> {
	}

	public static class PaymentCell extends AbstractCell<ExpensePaymentEntity> {

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
				ExpensePaymentEntity value, SafeHtmlBuilder sb) {
			sb.append(template.productCellTemplate(value.getName(),
					"123,43 PLN"));
		}
	}

	private static ExpenseViewImplUiBinder uiBinder = GWT
			.create(ExpenseViewImplUiBinder.class);

	@UiField(provided = true)
	CellTable<ExpenseConsumerEntity> consumersCellTable;

	@UiField
	EditableLabelWidget expenseNameWidget;

	@UiField
	HTMLPanel htmlPanel;

	@UiField
	PaymentsTableWidget paymentsTable;

	private Presenter presenter;

	@UiField
	RadioButton radioButtonAll;

	@UiField
	RadioButton radioButtonMe;

	@UiField
	RadioButton radioButtonSelected;

	public ExpenseViewImpl() {
		consumersCellTable = makeConsumersCellTable();
		initWidget(uiBinder.createAndBindUi(this));
		paymentsTable.setPresenter(this);
		radioButtonAll.setValue(true);
	}

	private CellTable<ExpenseConsumerEntity> makeConsumersCellTable() {
		BaseEntity.EntityKeyProvider<ExpenseConsumerEntity> keyProvider = new BaseEntity.EntityKeyProvider<ExpenseConsumerEntity>();

		final CellTable<ExpenseConsumerEntity> table = new CellTable<ExpenseConsumerEntity>(
				keyProvider);
		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

		CheckboxCell isConsumerCell = new CheckboxCell();
		Column<ExpenseConsumerEntity, Boolean> isConsumerColumn = new Column<ExpenseConsumerEntity, Boolean>(
				isConsumerCell) {

			@Override
			public Boolean getValue(ExpenseConsumerEntity object) {
				return object.isConsumer();
			}
		};
		table.addColumn(isConsumerColumn,
				ApplicationConstants.constants.getConsumer());

		TextColumn<ExpenseConsumerEntity> userNameColumn = new TextColumn<ExpenseConsumerEntity>() {

			@Override
			public String getValue(ExpenseConsumerEntity object) {
				return object.getName();
			}
		};
		table.addColumn(userNameColumn,
				ApplicationConstants.constants.userName());

		CheckboxCell isProportionalCell = new CheckboxCell();
		Column<ExpenseConsumerEntity, Boolean> isProportionalColumn = new Column<ExpenseConsumerEntity, Boolean>(
				isProportionalCell) {

			@Override
			public Boolean getValue(ExpenseConsumerEntity object) {
				return object.isProportional();
			}
		};
		table.addColumn(isProportionalColumn,
				ApplicationConstants.constants.proportional());

		final TextInputCell valueCell = new TextInputCell();
		Column<ExpenseConsumerEntity, String> valueColumn = new Column<ExpenseConsumerEntity, String>(
				valueCell) {

			@Override
			public String getValue(ExpenseConsumerEntity object) {
				return Double.toString(object.getValue());
			}
		};
		table.addColumn(valueColumn, ApplicationConstants.constants.value());
		return table;
	}

	@UiHandler("addParticipantButton")
	public void onAddParticipant(ClickEvent e) {
		this.presenter.addParticipants();
	}

	@UiHandler({ "radioButtonSelected", "radioButtonAll", "radioButtonMe" })
	void onChangeParticipants(ValueChangeEvent<Boolean> e) {
		consumersCellTable.setVisible(radioButtonSelected.getValue());
	}

	@UiHandler("expenseNameWidget")
	public void onExpenseNameChange(ValueChangeEvent<String> e) {
		presenter.setExpenseName(e.getValue());
	}

	@Override
	public void setConsumers(IdMap<ExpenseConsumerEntity> consumers) {
		if (consumers.getDataDisplays().contains(consumersCellTable))
			consumers.removeDataDisplay(consumersCellTable);
		consumers.addDataDisplay(consumersCellTable);
		consumersCellTable.redraw();
	}

	@Override
	public void setExpenseName(String name) {
		expenseNameWidget.setText(name);
	}

	@Override
	public void setPayments(IdMap<ExpensePaymentEntity> payments) {
		paymentsTable.setPayments(payments);
	}

	@Override
	public void setPaymentsSum(double value) {
		paymentsTable.setSum(value);
	}

	@Override
	public void setPaymentValue(Long userId, double value) {
		this.presenter.paymentSetValue(userId, value);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}
