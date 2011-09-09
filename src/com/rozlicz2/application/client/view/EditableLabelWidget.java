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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.rozlicz2.application.client.Validator;
import com.rozlicz2.application.client.Validator.ValidatorException;
import com.rozlicz2.application.client.resources.ApplicationResources;

public class EditableLabelWidget extends Composite implements HasText,
		HasValueChangeHandlers<String> {

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

	private static Resources DEFAULT_RESOURCES;

	private static EditableLabelWidgetUiBinder uiBinder = GWT
			.create(EditableLabelWidgetUiBinder.class);

	private static Resources getDefaultResources() {
		if (DEFAULT_RESOURCES == null) {
			DEFAULT_RESOURCES = GWT.create(Resources.class);
		}
		return DEFAULT_RESOURCES;
	}

	@UiField
	Button cancelButton;

	@UiField
	Label captionLabel;
	@UiField
	Anchor editAnchor;
	@UiField
	Label errorLabel;
	@UiField
	HTMLPanel labelContainer;

	@UiField
	Button saveButton;
	private final Style style;
	@UiField
	TextBox textBox;
	@UiField
	Label textLabel;

	public EditableLabelWidget() {
		this("");
	}

	public EditableLabelWidget(Resources resources, String text) {
		this.style = resources.widgetStyle();
		this.style.ensureInjected();
		initWidget(uiBinder.createAndBindUi(this));
		setText(text);
	}

	public EditableLabelWidget(Resources resources, String text, String caption) {
		this(resources, text);
		setCaption(caption);
	}

	public EditableLabelWidget(String text) {
		this(getDefaultResources(), text);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	private void doEditable(boolean editable) {
		saveButton.setVisible(editable);
		cancelButton.setVisible(editable);
		textBox.setVisible(editable);
		errorLabel.setVisible(editable);
		textLabel.setVisible(!editable);
		editAnchor.setVisible(!editable);
	}

	@Override
	public String getText() {
		return textLabel.getText();
	}

	private void isValid() throws ValidatorException {
		String text = textBox.getText();
		Validator.isNotToShort(text);
	}

	@UiHandler("cancelButton")
	void onCancel(ClickEvent e) {
		doEditable(false);
	}

	@UiHandler("textBox")
	void onChange(ValueChangeEvent<String> e) {
		validate();
	}

	@UiHandler("editAnchor")
	void onEdit(ClickEvent e) {
		textBox.setText(getText());
		validate();
		doEditable(true);
		textBox.setFocus(true);
		textBox.selectAll();
	}

	@UiHandler("textBox")
	void onKeyPress(KeyPressEvent e) {
		char keyCode = e.getCharCode();
		if (keyCode == '\r' || keyCode == '\r') {
			save();
		}
	}

	@UiHandler("textBox")
	void onKeyUpEvent(KeyUpEvent e) {
		validate();
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
			ValueChangeEvent.fire(this, getText());
		} catch (ValidatorException ex) {
		}
		;
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
	public void setText(String text) {
		textLabel.setText(text);
		doEditable(false);
	}

	private void validate() {
		try {
			isValid();
			saveButton.setEnabled(true);
			errorLabel.addStyleName(ApplicationResources.INSTANCE.css()
					.hideClass());
			textBox.removeStyleName(this.style.errorClass());
		} catch (ValidatorException e) {
			saveButton.setEnabled(false);
			errorLabel.setText(e.getMessage());
			errorLabel.removeStyleName(ApplicationResources.INSTANCE.css()
					.hideClass());
			textBox.addStyleName(this.style.errorClass());
		}
	}

}
