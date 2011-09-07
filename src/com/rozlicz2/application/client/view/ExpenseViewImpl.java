package com.rozlicz2.application.client.view;

import java.util.List;

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
import com.google.gwt.view.client.ProvidesKey;
import com.rozlicz2.application.client.resources.ApplicationConstants;

public class ExpenseViewImpl extends Composite implements ExpenseView {

	private static ExpenseViewImplUiBinder uiBinder = GWT
			.create(ExpenseViewImplUiBinder.class);

	interface ExpenseViewImplUiBinder extends UiBinder<Widget, ExpenseViewImpl> {
	}

	public ExpenseViewImpl() {
		consumersCellTable = makeConsumersCellTable();
		initWidget(uiBinder.createAndBindUi(this));
		radioButtonAll.setValue(true);
	}

	@UiField
	HTMLPanel htmlPanel;

	@UiField
	EditableLabelWidget expenseNameWidget;

	@UiField
	PaymentsTableWidget paymentsTable;
	
	@UiField(provided = true)
	CellTable<ExpenseConsumer> consumersCellTable;
	
	@UiField
	RadioButton radioButtonAll;
	
	@UiField
	RadioButton radioButtonSelected;
	
	@UiField
	RadioButton radioButtonMe;
	
	private Presenter presenter;

	public static class PaymentCell extends AbstractCell<ExpensePayment> {

		interface Template extends SafeHtmlTemplates {
			@Template("<div>{0}</div><div>{1}</div>")
			SafeHtml productCellTemplate(String name, String price);
		}

		public PaymentCell() {
			if (template == null)
				template = GWT.create(Template.class);
		}

		private static Template template;

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				ExpensePayment value, SafeHtmlBuilder sb) {
			sb.append(template.productCellTemplate(value.name, "123,43 PLN"));
		}
	}

	public static class ConsumerCell extends AbstractCell<ExpenseConsumer> {

		interface Template extends SafeHtmlTemplates {
			@Template("<div>{0}</div><div>{1}</div>")
			SafeHtml productCellTemplate(String name, String price);
		}

		public ConsumerCell() {
			if (template == null)
				template = GWT.create(Template.class);
		}

		private static Template template;

		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,
				ExpenseConsumer value, SafeHtmlBuilder sb) {
			sb.append(template.productCellTemplate(value.name, "123,45 PLN"));
		}
	}

	
	private CellTable<ExpenseConsumer> makeConsumersCellTable() {
		ProvidesKey<ExpenseConsumer> keyProvider = new ProvidesKey<ExpenseConsumer>() {

			@Override
			public Object getKey(ExpenseConsumer item) {
				if (item == null)
					return null;
				return item.userId;
			}
		};
		final CellTable<ExpenseConsumer> table = new CellTable<ExpenseConsumer>(
				keyProvider);
		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

		CheckboxCell isConsumerCell = new CheckboxCell();
		Column<ExpenseConsumer, Boolean> isConsumerColumn = new Column<ExpenseView.ExpenseConsumer, Boolean>(
				isConsumerCell) {

			@Override
			public Boolean getValue(ExpenseConsumer object) {
				return object.isConsumer;
			}
		};
		table.addColumn(isConsumerColumn,
				ApplicationConstants.constants.getConsumer());

		TextColumn<ExpenseConsumer> userNameColumn = new TextColumn<ExpenseView.ExpenseConsumer>() {

			@Override
			public String getValue(ExpenseConsumer object) {
				return object.name;
			}
		};
		table.addColumn(userNameColumn,
				ApplicationConstants.constants.userName());

		CheckboxCell isProportionalCell = new CheckboxCell();
		Column<ExpenseConsumer, Boolean> isProportionalColumn = new Column<ExpenseView.ExpenseConsumer, Boolean>(
				isProportionalCell) {

			@Override
			public Boolean getValue(ExpenseConsumer object) {
				return object.isProportional;
			}
		};
		table.addColumn(isProportionalColumn,
				ApplicationConstants.constants.proportional());

		final TextInputCell valueCell = new TextInputCell();
		Column<ExpenseConsumer, String> valueColumn = new Column<ExpenseView.ExpenseConsumer, String>(
				valueCell) {

			@Override
			public String getValue(ExpenseConsumer object) {
				return Double.toString(object.value);
			}
		};
		table.addColumn(valueColumn, ApplicationConstants.constants.value());
		return table;
	}

	@Override
	public void setExpenseName(String name) {
		expenseNameWidget.setText(name);
	}
	
	@UiHandler("expenseNameWidget")
	public void onExpenseNameChange(ValueChangeEvent<String> e) {
		presenter.setExpenseName(e.getValue());
	}

	@UiHandler("addParticipantButton")
	public void onAddParticipant(ClickEvent e) {
		this.presenter.addParticipants();
	}
	
	@UiHandler({"radioButtonSelected", "radioButtonAll", "radioButtonMe"})
	void onChangeParticipants(ValueChangeEvent<Boolean> e) {
		consumersCellTable.setVisible(radioButtonSelected.getValue());
}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	public void setPayments(List<ExpensePayment> payments) {
		paymentsTable.setPayments(payments);
	}
	
	@Override
	public void setPaymentsSum(double value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setConsumers(List<ExpenseConsumer> consumers) {
		consumersCellTable.setRowCount(consumers.size(), true);
		consumersCellTable.setRowData(consumers);
		consumersCellTable.redraw();
	}

}
