package com.rozlicz2.application.client.widgets;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorError;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.rozlicz2.application.client.resources.ApplicationResources;
import com.rozlicz2.application.client.widgets.events.HasSaveHandlers;
import com.rozlicz2.application.client.widgets.events.SaveEvent;
import com.rozlicz2.application.client.widgets.events.SaveHandler;

public class EditableLabelWidget extends Composite implements HasText,
		HasValueChangeHandlers<String>, LeafValueEditor<String>,
		HasEditorErrors<String>, HasSaveHandlers {

	interface EditableLabelWidgetUiBinder extends
			UiBinder<Widget, EditableLabelWidget> {
	}

	public interface Resources extends ClientBundle {
		@Source(Style.DEFAULT_CSS)
		Style widgetStyle();
	}

	@CssResource.ImportedWithPrefix("gwt-CellList")
	public interface Style extends CssResource {
		String DEFAULT_CSS = "com/rozlicz2/application/client/widgets/EditableLabelWidget.css";

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
	Label editAnchorLabel;
	@UiField
	Label errorLabel;

	@UiField
	FocusPanel focusPanel;

	private boolean isEditing;
	@UiField
	Button saveButton;
	private final Style style;
	@UiField
	TextBox textBox;

	@UiField
	Label textLabel;

	public EditableLabelWidget() {
		this("");
		isEditing = false;
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
	public HandlerRegistration addSaveHandler(SaveHandler handler) {
		return addHandler(handler, SaveEvent.getType());
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	private void cancel() {
		doEditable(false);
		textBox.setText(textLabel.getText());
		changed();
	}

	private void changed() {
		ValueChangeEvent.fire(this, getValue());
	}

	private void doEditable(boolean isEditing) {
		this.isEditing = isEditing;
		saveButton.setVisible(isEditing);
		cancelButton.setVisible(isEditing);
		textBox.setVisible(isEditing);
		focusPanel.setVisible(!isEditing);
		textLabel.setVisible(!isEditing);
	}

	private void edit() {
		doEditable(true);
		textBox.setFocus(true);
		textBox.selectAll();
	}

	private void exit() {
		// cancel if not changed
		if (!textBox.getText().equals(textLabel.getText()))
			return;
		cancel();
	}

	@Override
	public String getText() {
		return textBox.getText();
	}

	@Override
	public String getValue() {
		return getText();
	}

	@UiHandler("cancelButton")
	void onCancel(ClickEvent e) {
		if (!isEditing)
			return;
		cancel();
	}

	@UiHandler("cancelButton")
	public void onCancelButtonFocus(FocusEvent event) {
		if (isEditing)
			return;
		event.preventDefault();
	}

	@UiHandler("textBox")
	void onChange(ValueChangeEvent<String> e) {
		if (!isEditing)
			return;
		ValueChangeEvent.fire(this, getText());
	}

	@UiHandler("editAnchorLabel")
	void onEdit(ClickEvent e) {
		if (isEditing)
			return;
		edit();
	}

	@UiHandler("focusPanel")
	public void onFocusPanelFocus(FocusEvent event) {
		if (isEditing)
			return;
		edit();
	}

	@UiHandler("textBox")
	void onKeyPress(KeyPressEvent e) {
		char keyCode = e.getCharCode();
		if (keyCode == '\r' || keyCode == '\r') {
			e.preventDefault();
			save();
		}
	}

	@UiHandler("textBox")
	void onKeyUpEvent(KeyUpEvent e) {
		if (!isEditing)
			return;
		changed();
	}

	@UiHandler("saveButton")
	void onSave(ClickEvent e) {
		if (!isEditing)
			return;
		save();
	}

	@UiHandler("textBox")
	public void onTextBoxBlur(BlurEvent event) {
		if (!isEditing)
			return;
		exit();
	}

	private void save() {
		SaveEvent.fire(this);
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
		textBox.setText(text);

		doEditable(false);
	}

	@Override
	public void setValue(String value) {
		setText(value);
	}

	public void showError(String error) {
		if (error == null || error.isEmpty()) {
			errorLabel.removeStyleName(ApplicationResources.INSTANCE.css()
					.errorLabelShowClass());
			saveButton.setEnabled(true);
		} else {
			errorLabel.setText(error);
			errorLabel.addStyleName(ApplicationResources.INSTANCE.css()
					.errorLabelShowClass());
			saveButton.setEnabled(false);
		}
	}

	@Override
	public void showErrors(List<EditorError> errors) {
		String strErrors = "";
		for (EditorError editorError : errors) {
			strErrors += editorError.getMessage();
		}
		showError(strErrors);
	}

}
