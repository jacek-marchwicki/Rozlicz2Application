package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

public class ExpenseViewImpl extends Composite implements ExpenseView {

	private static ExpenseViewImplUiBinder uiBinder = GWT
			.create(ExpenseViewImplUiBinder.class);

	interface ExpenseViewImplUiBinder extends UiBinder<Widget, ExpenseViewImpl> {
	}

	public ExpenseViewImpl() {
		consumersCellList = makeConsumersCellList();
		paymentsCellList = makePaymentsCellList();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	EditableLabelWidget expenseNameWidget;

	@UiField(provided=true)
	CellList<ExpenseConsumer> consumersCellList;

	@UiField(provided=true)
	CellList<ExpensePayment> paymentsCellList;
	
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
			sb.append(template.productCellTemplate(value.name, "123,zł"));
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
			sb.append(template.productCellTemplate(value.name, "123,zł"));
		}
	}

	CellList<ExpensePayment> makePaymentsCellList() {
		ProvidesKey<ExpensePayment> keyProvider = new ProvidesKey<ExpensePayment>() {

			@Override
			public Object getKey(ExpensePayment item) {
				if (item == null)
					return null;
				return item.userId;
			}
		};

		CellList<ExpensePayment> cellList = new CellList<ExpensePayment>(
				new PaymentCell(), keyProvider);

		final SingleSelectionModel<ExpensePayment> selectionModel = new SingleSelectionModel<ExpensePayment>(
				keyProvider);
		cellList.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				ExpensePayment selectedObject = selectionModel
						.getSelectedObject();
				if (selectedObject == null)
					return;
				selectionModel.setSelected(selectedObject, false);
				// TODO do something with object
			}
		});
		return cellList;
	}
	
	CellList<ExpenseConsumer> makeConsumersCellList() {
		ProvidesKey<ExpenseConsumer> keyProvider = new ProvidesKey<ExpenseConsumer>() {

			@Override
			public Object getKey(ExpenseConsumer item) {
				if (item == null)
					return null;
				return item.userId;
			}
		};

		CellList<ExpenseConsumer> cellList = new CellList<ExpenseConsumer>(
				new ConsumerCell(), keyProvider);

		final SingleSelectionModel<ExpenseConsumer> selectionModel = new SingleSelectionModel<ExpenseConsumer>(
				keyProvider);
		cellList.setSelectionModel(selectionModel);
		selectionModel.addSelectionChangeHandler(new Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				ExpenseConsumer selectedObject = selectionModel
						.getSelectedObject();
				if (selectedObject == null)
					return;
				selectionModel.setSelected(selectedObject, false);
				// TODO do something with object
			}
		});
		return cellList;
	}

	@Override
	public void setExpenseName(String name) {
		expenseNameWidget.setText(name);
	}

	@UiHandler("expenseNameWidget")
	public void onExpenseNameChange(ValueChangeEvent<String> e) {
		presenter.setExpenseName(e.getValue());
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setPayments(List<ExpensePayment> payments) {
		paymentsCellList.setRowCount(payments.size(), true);
		paymentsCellList.setRowData(payments);
		paymentsCellList.redraw();
	}

	@Override
	public void setPaymentsSum(double value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setConsumers(List<ExpenseConsumer> consumers) {
		consumersCellList.setRowCount(consumers.size(),true);
		consumersCellList.setRowData(consumers);
		consumersCellList.redraw();
	}

}
