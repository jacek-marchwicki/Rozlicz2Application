package com.rozlicz2.application.client.widgets;

import java.util.List;

import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.rozlicz2.application.client.widgets.events.HasSaveHandlers;
import com.rozlicz2.application.client.widgets.events.SaveEvent;
import com.rozlicz2.application.client.widgets.events.SaveHandler;
import com.rozlicz2.application.shared.proxy.ExpensePaymentEntityProxy;

public class PaymentsTableWidget extends Composite
		implements
		IsEditor<ListEditor<ExpensePaymentEntityProxy, ExpensePaymentEntityWidget>>,
		HasValueChangeHandlers<List<ExpensePaymentEntityProxy>>,
		HasSaveHandlers {

	private final class EditorSourceExtension extends
			EditorSource<ExpensePaymentEntityWidget> {

		@Override
		public ExpensePaymentEntityWidget create(int index) {
			ExpensePaymentEntityWidget editor = new ExpensePaymentEntityWidget();
			editor.addValueChangeHandler(new ValueChangeHandlerImplementation());
			flowPanel.insert(editor, index);
			return editor;
		}

		@Override
		public void dispose(ExpensePaymentEntityWidget subEditor) {
			subEditor.removeFromParent();
		}

		@Override
		public void setIndex(ExpensePaymentEntityWidget editor, int index) {
			flowPanel.insert(editor, index);
		}

	}

	interface PaymentsTableWidgetUiBinder extends
			UiBinder<Widget, PaymentsTableWidget> {
	}

	private final class ValueChangeHandlerImplementation implements
			ValueChangeHandler<ExpensePaymentEntityProxy> {
		@Override
		public void onValueChange(
				ValueChangeEvent<ExpensePaymentEntityProxy> event) {
			ValueChangeEvent.fire(PaymentsTableWidget.this,
					listEditor.getList());
		}
	}

	private static PaymentsTableWidgetUiBinder uiBinder = GWT
			.create(PaymentsTableWidgetUiBinder.class);

	@UiField
	Label errorLabel;

	@UiField
	FlowPanel flowPanel;

	ListEditor<ExpensePaymentEntityProxy, ExpensePaymentEntityWidget> listEditor;

	private final EditorSource<ExpensePaymentEntityWidget> source = new EditorSourceExtension();

	public PaymentsTableWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		listEditor = ListEditor.of(source);
	}

	@Override
	public HandlerRegistration addSaveHandler(SaveHandler handler) {
		return addHandler(handler, SaveEvent.getType());
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<List<ExpensePaymentEntityProxy>> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public ListEditor<ExpensePaymentEntityProxy, ExpensePaymentEntityWidget> asEditor() {
		return listEditor;
	}
}
