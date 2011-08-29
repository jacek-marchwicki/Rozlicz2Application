package com.rozlicz2.application.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.ImportedWithPrefix;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.rozlicz2.application.client.Validator;
import com.rozlicz2.application.client.Validator.ValidatorException;

public class EditableLabelWidget extends Composite implements HasText,
		HasValueChangeHandlers<String> {

	private static EditableLabelWidgetUiBinder uiBinder = GWT
			.create(EditableLabelWidgetUiBinder.class);

	interface EditableLabelWidgetUiBinder extends
			UiBinder<Widget, EditableLabelWidget> {
	}

	public interface Resources extends ClientBundle {
		@Source(Style.DEFAULT_CSS)
	    Style widgetStyle();
	}

	@ImportedWithPrefix("gwt-CellList")
	public interface Style extends CssResource {
		String DEFAULT_CSS = "com/rozlicz2/application/client/view/EditableLabelWidget.css";
		
		String errorClass();
	}
	
	private final Style style;
	private static Resources DEFAULT_RESOURCES;
	private static Resources getDefaultResources() {
	    if (DEFAULT_RESOURCES == null) {
	      DEFAULT_RESOURCES = GWT.create(Resources.class);
	    }
	    return DEFAULT_RESOURCES;
	  }

	@UiField
	Button saveButton;
	@UiField
	Button cancelButton;
	@UiField
	TextBox textBox;
	@UiField
	Label errorLabel;

	@UiField
	Label textLabel;
	@UiField
	Anchor editAnchor;
	
	private void doEditable(boolean editable) {
		saveButton.setVisible(editable);
		cancelButton.setVisible(editable);
		textBox.setVisible(editable);
		errorLabel.setVisible(editable);
		textLabel.setVisible(!editable);
		editAnchor.setVisible(!editable);
	}
	
	public EditableLabelWidget() {
		this("");
	}
	
	public EditableLabelWidget(String text) {
		this(getDefaultResources(), text);
	}
	public EditableLabelWidget(Resources resources, String text) {
		this.style = resources.widgetStyle();
		this.style.ensureInjected();
		initWidget(uiBinder.createAndBindUi(this));
		setText(text);
	}

	@UiHandler("editAnchor")
	void onEdit(ClickEvent e) {
		textBox.setText(getText());
		validate();
		doEditable(true);
		textBox.setFocus(true);
		textBox.selectAll();
	}

	@UiHandler("saveButton")
	void onSave(ClickEvent e) {
		save();
	}

	private void save() {
		try {
			isValid();
			setText(textBox.getText());
			doEditable(false);
			ValueChangeEvent.fire(EditableLabelWidget.this, getText());
		} catch (ValidatorException ex) {
		}
		;
	}

	@UiHandler("cancelButton")
	void onCancel(ClickEvent e) {
		doEditable(false);
	}

	@UiHandler("textBox")
	void onChange(ValueChangeEvent<String> e) {
		validate();
	}

	@UiHandler("textBox")
	void onKeyUpEvent(KeyUpEvent e) {
		validate();
	}

	@UiHandler("textBox")
	void onKeyPress(KeyPressEvent e) {
		char keyCode = e.getCharCode();
		if (keyCode == '\r' || keyCode == '\r') {
			save();
		}
	}

	private void validate() {
		try {
			isValid();
			saveButton.setEnabled(true);
			errorLabel.setText("");
			textBox.removeStyleName(this.style.errorClass());
		} catch (ValidatorException e) {
			saveButton.setEnabled(false);
			errorLabel.setText(e.getMessage());
			textBox.addStyleName(this.style.errorClass());
		}
	}

	private void isValid() throws ValidatorException {
		String text = textBox.getText();
		Validator.isNotToShort(text);
	}

	public void setText(String text) {
		textLabel.setText(text);
		doEditable(false);
	}

	public String getText() {
		return textLabel.getText();
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

}
