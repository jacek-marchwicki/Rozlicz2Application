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
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.rozlicz2.application.shared.proxy.ExpenseConsumerEntityProxy;

public class ConsumersTableWidget extends Composite
		implements
		IsEditor<ListEditor<ExpenseConsumerEntityProxy, ConsumersTableWidget.ExpenseConsumerEntityEditor>>,
		HasValueChangeHandlers<List<ExpenseConsumerEntityProxy>> {

	interface ConsumersTableWidgetUiBinder extends
			UiBinder<Widget, ConsumersTableWidget> {
	}

	private final class EditorSourceExtension extends
			EditorSource<ConsumersTableWidget.ExpenseConsumerEntityEditor> {

		@Override
		public ExpenseConsumerEntityEditor create(int index) {
			ExpenseConsumerEntityEditor editor = new ExpenseConsumerEntityEditor();
			this.setIndex(editor, index);
			return editor;
		}

		@Override
		public void dispose(ExpenseConsumerEntityEditor subEditor) {
			subEditor.getIsConsumerEditor().removeFromParent();
			subEditor.getNameEditor().removeFromParent();
			subEditor.getIsProportionalEditor().removeFromParent();
			subEditor.getValue().removeFromParent();
		}

		@Override
		public void setIndex(ExpenseConsumerEntityEditor editor, int index) {
			flexTable.setWidget(index + 1, 0, editor.getIsConsumerEditor());
			flexTable.setWidget(index + 1, 1, editor.getNameEditor());
			flexTable.setWidget(index + 1, 2, editor.getIsProportionalEditor());
			flexTable.setWidget(index + 1, 3, editor.getValue());
		}

	}

	public class ExpenseConsumerEntityEditor implements
			Editor<ExpenseConsumerEntityProxy> {

		public CheckBox isConsumerEditor;
		public CheckBox isProportionalEditor;
		public Label nameEditor;
		public CurrencyWidget valueEditor;

		public ExpenseConsumerEntityEditor() {
			nameEditor = new Label();
			isConsumerEditor = new CheckBox();
			isProportionalEditor = new CheckBox();
			valueEditor = new CurrencyWidget();
		}

		public Widget getIsConsumerEditor() {
			return isConsumerEditor;
		}

		public Widget getIsProportionalEditor() {
			return isProportionalEditor;
		}

		public Widget getNameEditor() {
			return nameEditor;
		}

		public Widget getValue() {
			return valueEditor;
		}

	}

	private static ConsumersTableWidgetUiBinder uiBinder = GWT
			.create(ConsumersTableWidgetUiBinder.class);

	@UiField
	Label errorLabel;

	@UiField
	FlexTable flexTable;

	private final ListEditor<ExpenseConsumerEntityProxy, ExpenseConsumerEntityEditor> listEditor;

	private final EditorSource<ExpenseConsumerEntityEditor> source = new EditorSourceExtension();

	public ConsumersTableWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		flexTable.setText(0, 0, "Is Participant");
		flexTable.setText(0, 1, "Name");
		flexTable.setText(0, 2, "Proportional");
		flexTable.setText(0, 3, "Value");
		listEditor = ListEditor.of(source);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<List<ExpenseConsumerEntityProxy>> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public ListEditor<ExpenseConsumerEntityProxy, ExpenseConsumerEntityEditor> asEditor() {
		return listEditor;
	}

}
