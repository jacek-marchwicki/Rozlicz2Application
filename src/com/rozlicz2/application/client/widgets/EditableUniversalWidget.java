package com.rozlicz2.application.client.widgets;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.HasEditorDelegate;
import com.google.gwt.editor.client.HasEditorErrors;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.rozlicz2.application.client.resources.ApplicationResources;

public abstract class EditableUniversalWidget<T> extends Composite implements
		HasValueChangeHandlers<T>, LeafValueEditor<T>, HasEditorErrors<T>,
		HasValue<T>, HasEditorDelegate<T> {

	interface EditableUniversalWidgetUiBinder extends
			UiBinder<Widget, EditableUniversalWidget<?>> {
	}

	public interface Resources extends ClientBundle {
		@Source(Style.DEFAULT_CSS)
		Style widgetStyle();
	}

	public interface Style extends CssResource {
		String DEFAULT_CSS = "com/rozlicz2/application/client/widgets/EditableUniversalWidget.css";

		String errorClass();
	}

	private static EditableUniversalWidgetUiBinder uiBinder = GWT
			.create(EditableUniversalWidgetUiBinder.class);

	@UiField
	Label captionLabel;
	protected EditorDelegate<T> delegate;
	@UiField
	Label disabledLabel;

	@UiField
	Label editAnchorLabel;

	private boolean editing = false;
	private boolean enabled = true;
	@UiField
	Label errorLabel;
	@UiField
	FocusPanel focusPanel;

	@UiField
	TextBox textBox;

	@UiField
	Label textLabel;

	public EditableUniversalWidget() {
		initWidget(uiBinder.createAndBindUi(this));
		editing = false;
		setVisibility();
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<T> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	private void changed() {
		ValueChangeEvent.fire(this, getValue());
	}

	protected abstract T convertFromString(String string);

	protected String convertToEditableText(T value) {
		return convertToText(value);
	}

	protected abstract String convertToText(T value);

	private void edit() {
		this.editing = true;
		setVisibility();
		textBox.setFocus(true);
		textBox.selectAll();
	}

	protected void extendedOnKeyPressEvent(KeyPressEvent e) {
	}

	@Override
	public T getValue() {
		return convertFromString(textBox.getText());
	}

	@UiHandler("textBox")
	void onChange(ValueChangeEvent<String> e) {
		if (!editing)
			return;
		changed();
	}

	@UiHandler("editAnchorLabel")
	void onEdit(ClickEvent e) {
		if (editing)
			return;
		edit();
	}

	@UiHandler("focusPanel")
	public void onFocusPanelFocus(FocusEvent event) {
		if (editing)
			return;
		edit();
	}

	@UiHandler("textBox")
	void onKeyPress(KeyPressEvent e) {
		char keyCode = e.getCharCode();
		if (keyCode == '\r' || keyCode == '\r') {
			e.preventDefault();
		}
		extendedOnKeyPressEvent(e);
	}

	@UiHandler("textBox")
	void onKeyUpEvent(KeyUpEvent e) {
		if (!editing)
			return;
		changed();
	}

	@UiHandler("textBox")
	public void onTextBoxBlur(BlurEvent event) {
		if (!editing)
			return;
		T value = convertFromString(textBox.getText());
		if (value == null) {
			setText(textBox.getText());
		} else {
			setText(convertToText(value));
		}
		editing = false;
		setVisibility();
	}

	public void setCaption(String caption) {
		if (caption.isEmpty()) {
			captionLabel.setVisible(false);
			return;
		}
		captionLabel.setText(caption);
		captionLabel.setVisible(true);
	}

	@Override
	public void setDelegate(EditorDelegate<T> delegate) {
		this.delegate = delegate;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		setVisibility();
	}

	private void setText(String text) {
		textLabel.setText(text);
		disabledLabel.setText(text);
	}

	@Override
	public void setValue(T value) {
		setValue(value, false);
	}

	@Override
	public void setValue(T value, boolean fireEvents) {
		setText(convertToText(value));
		textBox.setText(convertToEditableText(value));
		if (fireEvents) {
			ValueChangeEvent.fire(this, value);
		}
	}

	private void setVisibility() {
		textBox.setVisible(editing);
		focusPanel.setVisible(!editing);
		textLabel.setVisible(!editing);
		editAnchorLabel.setVisible(!editing && enabled);
		focusPanel.setVisible(enabled);
		disabledLabel.setVisible(!enabled);
	}

	public void showError(String error) {
		if (error == null || error.isEmpty()) {
			errorLabel.removeStyleName(ApplicationResources.INSTANCE.css()
					.errorLabelShowClass());
		} else {
			errorLabel.setText(error);
			errorLabel.addStyleName(ApplicationResources.INSTANCE.css()
					.errorLabelShowClass());
		}
	}

	@Override
	public void showErrors(List<EditorError> errors) {
		String strErrors = "";
		for (EditorError editorError : errors) {
			strErrors += editorError.getMessage();
		}
		showError(strErrors);
		if (errors.size() > 0) {
			textLabel.addStyleName(ApplicationResources.INSTANCE.css()
					.editableWidgetLabelError());
		} else {
			textLabel.removeStyleName(ApplicationResources.INSTANCE.css()
					.editableWidgetLabelError());
		}
	}

}
