package com.rozlicz2.application.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class AddedParticipantWidget extends Composite implements HasText, HasClickHandlers {

	private static AddedParticipantWidgetUiBinder uiBinder = GWT
			.create(AddedParticipantWidgetUiBinder.class);

	interface AddedParticipantWidgetUiBinder extends
			UiBinder<Widget, AddedParticipantWidget> {
	}

	public AddedParticipantWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	Button removeButton;
	
	@UiField
	Label userLabel;

	@UiHandler("removeButton")
	void onClick(ClickEvent e) {
		ClickEvent.fireNativeEvent(e.getNativeEvent(), this);
	}

	public void setText(String text) {
		userLabel.setText(text);
	}

	public String getText() {
		return userLabel.getText();
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addHandler(handler, ClickEvent.getType());
	}

}
