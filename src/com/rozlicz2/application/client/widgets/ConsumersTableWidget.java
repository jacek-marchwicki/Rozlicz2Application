package com.rozlicz2.application.client.widgets;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.IsEditor;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.rozlicz2.application.client.resources.ApplicationResources;
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

		private final class ValueChangeHandlerImplementation implements
				ValueChangeHandler<ExpenseConsumerEntityProxy> {
			@Override
			public void onValueChange(
					ValueChangeEvent<ExpenseConsumerEntityProxy> event) {
				changed();
			}
		}

		private final String evenCellClass = ApplicationResources.INSTANCE
				.css().flexTableEvenClass();
		private final String oddCellClass = ApplicationResources.INSTANCE.css()
				.flexTableOddClass();

		@Override
		public ExpenseConsumerEntityEditor create(int index) {
			ExpenseConsumerEntityEditor editor = new ExpenseConsumerEntityEditor();
			editor.addValueChangeHandler(new ValueChangeHandlerImplementation());
			this.setIndex(editor, index);
			return editor;
		}

		@Override
		public void dispose(ExpenseConsumerEntityEditor subEditor) {
			subEditor.getConsumerEditor().removeFromParent();
			subEditor.getNameEditor().removeFromParent();
			subEditor.getProportionalEditor().removeFromParent();
			subEditor.getValue().removeFromParent();
		}

		@Override
		public void setIndex(ExpenseConsumerEntityEditor editor, int index) {
			String cellClass;
			if (index % 2 == 0) {
				cellClass = evenCellClass;
			} else
				cellClass = oddCellClass;
			flexTable.setWidget(index + 1, 0, editor.getNameEditor());
			flexTable.setWidget(index + 1, 1, editor.getConsumerEditor());
			flexTable.setWidget(index + 1, 2, editor.getProportionalEditor());
			flexTable.setWidget(index + 1, 3, editor.getValue());
			for (int i = 0; i < 4; i++)
				flexTable.getFlexCellFormatter().setStyleName(index + 1, i,
						cellClass);
		}

	}

	public class ExpenseConsumerEntityEditor implements
			ValueAwareEditor<ExpenseConsumerEntityProxy>,
			HasValueChangeHandlers<ExpenseConsumerEntityProxy> {

		public CheckBox consumerEditor;
		private HandlerManager handlerManager;
		public Label nameEditor;
		public CheckBox proportionalEditor;
		private ExpenseConsumerEntityProxy value;
		public CurrencyWidget valueEditor;

		public ExpenseConsumerEntityEditor() {
			nameEditor = new Label();
			consumerEditor = new CheckBox();
			proportionalEditor = new CheckBox();
			valueEditor = new CurrencyWidget();
			ValueChangeHandler<Boolean> changeHandler = new ValueChangeHandler<Boolean>() {

				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					changed();
				}
			};
			consumerEditor.addValueChangeHandler(changeHandler);
			proportionalEditor.addValueChangeHandler(changeHandler);
			valueEditor.addValueChangeHandler(new ValueChangeHandler<Double>() {

				@Override
				public void onValueChange(ValueChangeEvent<Double> event) {
					changed();
				}
			});
		}

		@Override
		public HandlerRegistration addValueChangeHandler(
				ValueChangeHandler<ExpenseConsumerEntityProxy> handler) {
			return ensureHandlers().addHandler(ValueChangeEvent.getType(),
					handler);
		}

		protected void changed() {
			ValueChangeEvent.fire(this, value);
		}

		HandlerManager ensureHandlers() {
			return handlerManager == null ? handlerManager = createHandlerManager()
					: handlerManager;
		}

		@Override
		public void fireEvent(GwtEvent<?> event) {
			if (handlerManager != null) {
				handlerManager.fireEvent(event);
			}
		}

		@Override
		public void flush() {
			if (!value.isConsumer()) {
				proportionalEditor.setEnabled(false);
				valueEditor.setEnabled(false);
				return;
			}
			proportionalEditor.setEnabled(true);
			if (value.isProportional()) {
				valueEditor.setEnabled(false);
				return;
			}
			valueEditor.setEnabled(true);
		}

		public Widget getConsumerEditor() {
			return consumerEditor;
		}

		public Widget getNameEditor() {
			return nameEditor;
		}

		public Widget getProportionalEditor() {
			return proportionalEditor;
		}

		public Widget getValue() {
			return valueEditor;
		}

		@Override
		public void onPropertyChange(String... paths) {

		}

		@Override
		public void setDelegate(
				EditorDelegate<ExpenseConsumerEntityProxy> delegate) {
			delegate.subscribe();
		}

		@Override
		public void setValue(ExpenseConsumerEntityProxy value) {
			this.value = value;
			if (!value.isConsumer()) {
				proportionalEditor.setEnabled(false);
				valueEditor.setEnabled(false);
				return;
			}
			proportionalEditor.setEnabled(true);
			if (value.isProportional()) {
				valueEditor.setEnabled(false);
				return;
			}
			valueEditor.setEnabled(true);
		}

	}

	private static ConsumersTableWidgetUiBinder uiBinder = GWT
			.create(ConsumersTableWidgetUiBinder.class);

	@UiField
	Label errorLabel;

	@UiField
	FlexTable flexTable;

	private final String headerClass = ApplicationResources.INSTANCE.css()
			.flexTableHeaderClass();

	private final ListEditor<ExpenseConsumerEntityProxy, ExpenseConsumerEntityEditor> listEditor;

	private final EditorSource<ExpenseConsumerEntityEditor> source = new EditorSourceExtension();

	public ConsumersTableWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		flexTable.setText(0, 0, "Name");
		flexTable.setText(0, 1, "Is Participant");
		flexTable.setText(0, 2, "Proportional");
		flexTable.setText(0, 3, "Value");
		for (int i = 0; i < 4; i++)
			flexTable.getFlexCellFormatter().setStyleName(0, i, headerClass);
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

	protected void changed() {
		ValueChangeEvent.fire(this, listEditor.getList());
	}

}
