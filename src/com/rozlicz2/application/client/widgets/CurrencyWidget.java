package com.rozlicz2.application.client.widgets;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.HasEditorDelegate;
import com.google.gwt.editor.client.HasEditorErrors;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class CurrencyWidget extends Composite implements IsWidget,
		HasValueChangeHandlers<Double>, LeafValueEditor<Double>,
		HasEditorErrors<Double>, HasValue<Double>, HasEditorDelegate<Double> {

	interface CurrencyWidgetUiBinder extends UiBinder<Widget, CurrencyWidget> {
	}

	private static CurrencyWidgetUiBinder uiBinder = GWT
			.create(CurrencyWidgetUiBinder.class);

	private EditorDelegate<Double> delegate;

	@UiField
	Label errorLabel;

	boolean isEditing = false;

	@UiField
	TextBox textBox;

	private Double value = 0.0;

	public CurrencyWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Double> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	private void changed() {
		try {
			this.value = NumberFormat.getDecimalFormat().parse(
					textBox.getValue());
		} catch (NumberFormatException ex) {
		}
		ValueChangeEvent.fire(this, this.value);
	}

	@Override
	public Double getValue() {
		if (isEditing) {
			try {
				this.value = NumberFormat.getDecimalFormat().parse(
						textBox.getValue());
			} catch (NumberFormatException ex) {
				delegate.recordError("Wrong integer value", textBox.getValue(),
						textBox.getValue());
			}
		}
		return value;
	}

	@UiHandler("textBox")
	void onChange(ValueChangeEvent<String> e) {
		changed();
	}

	@UiHandler("textBox")
	public void onTextBoxBlur(BlurEvent e) {
		isEditing = false;
		setValue();
	}

	@UiHandler("textBox")
	public void onTextBoxFocus(FocusEvent e) {
		isEditing = true;
		setValue();
	}

	@UiHandler("textBox")
	public void onTextBoxKeyPress(KeyPressEvent e) {
		char charCode = e.getCharCode();
		if (charCode == ',')
			return;
		if (charCode == '.')
			return;
		if (charCode >= '0' && charCode <= '9')
			return;
		e.preventDefault();
	}

	@UiHandler("textBox")
	void onTextBoxKeyUp(KeyUpEvent e) {
		changed();
	}

	@Override
	public void setDelegate(EditorDelegate<Double> delegate) {
		this.delegate = delegate;
	}

	private void setValue() {
		String textValue;
		if (isEditing) {
			textValue = NumberFormat.getDecimalFormat().format(value);
		} else {
			textValue = NumberFormat.getCurrencyFormat().format(value);
		}
		textBox.setValue(textValue, false);

	}

	@Override
	public void setValue(Double value) {
		setValue(value, true);
	}

	@Override
	public void setValue(Double value, boolean fireEvents) {
		this.value = value;
		setValue();
	}

	@Override
	public void showErrors(List<EditorError> errors) {
		StringBuilder b = new StringBuilder();
		for (EditorError editorError : errors) {
			String message = editorError.getMessage();
			b.append(message);
		}
		errorLabel.setText(b.toString());
	}

}
