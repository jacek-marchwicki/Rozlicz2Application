package com.rozlicz2.application.client.view;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;

public class AddParticipantWidget extends Composite implements ClickHandler, AddParticipantView {

	private static AddParticipantWidgetUiBinder uiBinder = GWT
			.create(AddParticipantWidgetUiBinder.class);

	interface AddParticipantWidgetUiBinder extends
			UiBinder<Widget, AddParticipantWidget> {
	}

	private static MyTemplate template;

	public AddParticipantWidget() {
		if (template == null)
			template = GWT.create(MyTemplate.class);
		initWidget(uiBinder.createAndBindUi(this));
		allowEditCheckBox.setEnabled(false);
		inviteCheckBox.setEnabled(false);
	}
	
	interface MyTemplate extends SafeHtmlTemplates {
		@Template("{0}")
		SafeHtml productUserTemplate(String name);
	}
	
	@UiField
	HTMLPanel shadowPanel;
	
	@UiField
	PopupPanel mainPanel;

	@UiField
	SuggestBox suggestBox;
	
	@UiField
	FlowPanel flowPanel;
	
	@UiField
	CheckBox inviteCheckBox;
	
	@UiField
	CheckBox allowEditCheckBox;
	
	@UiField
	Button saveButton;
	
	@UiField
	Button cancelButton;
	
	@UiFactory
	public SuggestBox createSuggestBox() {
		oracle = new MultiWordSuggestOracle();
		SuggestBox suggestBox = new SuggestBox(oracle);
		return suggestBox;
	}
	
	HashMap<String, String> users = new HashMap<String, String>();

	private MultiWordSuggestOracle oracle;

	private Presenter presenter;
	
	@UiHandler("suggestBox")
	public void onKeyPress(KeyPressEvent e) {
		char key = e.getCharCode();
		if (key != '\r' && key != '\n' && key != ',')
			return;
		String name = suggestBox.getValue().trim();
		suggestBox.setValue("");
		e.preventDefault();
		if (name.isEmpty())
			return;
		AddedParticipantWidget widget = new AddedParticipantWidget();
		widget.setText(name);
		widget.addClickHandler(this);
		flowPanel.insert(widget, 0);
		users.put(name, name);
		setInviteVisibility();
	}
	
	public boolean isEmailOnList() {
		Collection<String> usersCollection = users.values();
		for (String user : usersCollection) {
			boolean isEmail = user.matches("^[A-z0-9._%+-]+@[A-z0-9.-]+\\.[A-z]{2,4}$");
			if (isEmail)
				return true;
		}
		return false;
	}
	
	public void setInviteVisibility() {
		if (!isEmailOnList())
		{
			allowEditCheckBox.setEnabled(false);
			inviteCheckBox.setEnabled(false);
			return;
		}
		allowEditCheckBox.setEnabled(true);
		inviteCheckBox.setEnabled(true);
	}

	@Override
	public void onClick(ClickEvent event) {
		Object source = event.getSource();
		if ( source instanceof AddedParticipantWidget)
		{
			AddedParticipantWidget addedWidget = 
				(AddedParticipantWidget)source;
			String userName = addedWidget.getText();
			users.remove(userName);
			flowPanel.remove(addedWidget);
			setInviteVisibility();
		}
	}
	
	@UiHandler("saveButton")
	public void onSave(ClickEvent e) {
		presenter.addedUsers(users.values());
	}
	
	@UiHandler("cancelButton")
	public void onCancel(ClickEvent e) {
		presenter.cancel();
	}

	@Override
	public void setUsersList(List<String> users) {
		oracle.clear();
		for (String user: users) {
			oracle.add(user);
		}
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void center() {
		int positionLeft = shadowPanel.getOffsetWidth();
		positionLeft -= mainPanel.getOffsetWidth();
		positionLeft /= 2;
		positionLeft += shadowPanel.getAbsoluteLeft();
		int positionTop = shadowPanel.getOffsetHeight();
		positionTop -= mainPanel.getOffsetHeight();
		positionTop /= 2;
		mainPanel.setPopupPosition(positionLeft, positionTop);
	}

}
