package com.rozlicz2.application.client.widgets;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.rozlicz2.application.client.resources.ApplicationResources;
import com.rozlicz2.application.shared.proxy.ExpensePaymentEntityProxy;

public class PaymentsTableWidget extends Composite
		implements
		IsEditor<ListEditor<ExpensePaymentEntityProxy, PaymentsTableWidget.ExpensePaymentEntityWidget>>,
		HasValueChangeHandlers<List<ExpensePaymentEntityProxy>> {

	private final class EditorSourceExtension extends
			EditorSource<ExpensePaymentEntityWidget> {

		private final String evenCellClass = ApplicationResources.INSTANCE
				.css().flexTableEvenClass();
		private final String oddCellClass = ApplicationResources.INSTANCE.css()
				.flexTableOddClass();

		@Override
		public ExpensePaymentEntityWidget create(int index) {
			ExpensePaymentEntityWidget editor = new ExpensePaymentEntityWidget();
			this.setIndex(editor, index);
			editor.getHasValueChangeHandlers().addValueChangeHandler(
					new ValueChangeHandlerImplementation());
			return editor;
		}

		@Override
		public void dispose(ExpensePaymentEntityWidget subEditor) {
			subEditor.getNameEditor().removeFromParent();
			subEditor.getValueEditor().removeFromParent();
			// TODO may cause unexpected appirence widgets
		}

		@Override
		public void setIndex(ExpensePaymentEntityWidget editor, int index) {
			flexTable.setWidget(index + 1, 0, editor.getNameEditor());
			flexTable.setWidget(index + 1, 1, editor.getValueEditor());
			String cellClass;
			if (index % 2 == 0) {
				cellClass = evenCellClass;
			} else
				cellClass = oddCellClass;
			flexTable.getFlexCellFormatter().setStyleName(index + 1, 0,
					cellClass);
			flexTable.getFlexCellFormatter().setStyleName(index + 1, 1,
					cellClass);

		}

	}

	public static class ExpensePaymentEntityWidget implements
			Editor<ExpensePaymentEntityProxy> {

		/*
		 * Must be public cause is handled by editor framework
		 */
		public final Label nameEditor;

		/*
		 * Must be public cause is handled by editor framework
		 */
		public final CurrencyWidget valueEditor;

		public ExpensePaymentEntityWidget() {
			nameEditor = new Label();
			valueEditor = new CurrencyWidget();
		}

		public HasValueChangeHandlers<Double> getHasValueChangeHandlers() {
			return valueEditor;
		}

		public Widget getNameEditor() {
			return nameEditor;
		}

		public Widget getValueEditor() {
			return valueEditor;
		}
	}

	interface PaymentsTableWidgetUiBinder extends
			UiBinder<Widget, PaymentsTableWidget> {
	}

	private final class ValueChangeHandlerImplementation implements
			ValueChangeHandler<Double> {
		@Override
		public void onValueChange(ValueChangeEvent<Double> event) {
			ValueChangeEvent.fire(PaymentsTableWidget.this,
					listEditor.getList());
		}
	}

	private static PaymentsTableWidgetUiBinder uiBinder = GWT
			.create(PaymentsTableWidgetUiBinder.class);

	@UiField
	Label errorLabel;

	@UiField
	FlexTable flexTable;

	private final String headerClass = ApplicationResources.INSTANCE.css()
			.flexTableHeaderClass();

	ListEditor<ExpensePaymentEntityProxy, ExpensePaymentEntityWidget> listEditor;

	private final EditorSource<ExpensePaymentEntityWidget> source = new EditorSourceExtension();

	public PaymentsTableWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		flexTable.setText(0, 0, "Name");
		flexTable.setText(0, 1, "Value");
		flexTable.getFlexCellFormatter().setStyleName(0, 0, headerClass);
		flexTable.getFlexCellFormatter().setStyleName(0, 1, headerClass);
		listEditor = ListEditor.of(source);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<List<ExpensePaymentEntityProxy>> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public ListEditor<ExpensePaymentEntityProxy, PaymentsTableWidget.ExpensePaymentEntityWidget> asEditor() {
		return listEditor;
	}
}
