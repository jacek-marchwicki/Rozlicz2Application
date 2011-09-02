package com.rozlicz2.application.client.view;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;

public class AddParticipantWidget extends Composite implements ClickHandler,
		AddParticipantView {

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
		suggestBox.getTextBox().addBlurHandler(new BlurHandler() {
			
			@Override
			public void onBlur(BlurEvent event) {
				onSuggestBoxBlur(event);
			}
		});
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
	FocusPanel focusPanel;

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
	public SuggestBox makeSuggestBox() {
		oracle = new MultiWordSuggestOracle();
		SuggestBox suggestBox = new SuggestBox(oracle);
		return suggestBox;
	}

	HashMap<String, String> users = new HashMap<String, String>();

	private MultiWordSuggestOracle oracle;

	private Presenter presenter;

	@UiHandler("focusPanel")
	public void onFocusPanelClick(ClickEvent e) {
		suggestBox.setFocus(true);
	}

	private void addUser(String user) {
		String name = user.trim();
		if (name.isEmpty())
			return;
		if (users.containsKey(name))
			return;
		AddedParticipantWidget widget = new AddedParticipantWidget();
		widget.setText(name);
		widget.addClickHandler(this);
		int lastWidget = flowPanel.getWidgetCount();
		flowPanel.insert(widget, lastWidget-1);
		users.put(name, name);
		setVisibility();
	}
	private int hackKeyUp = 0;
	@UiHandler("suggestBox")
	public void onKeyUp(KeyDownEvent e) {
		if (++hackKeyUp % 2 == 0)
			return;
		int key = e.getNativeKeyCode();
		if (key == KeyCodes.KEY_BACKSPACE && suggestBox.getValue().isEmpty()) {
			removeLast();
			e.preventDefault();
			e.stopPropagation();
		}
	}
	
	public void onSuggestBoxBlur(BlurEvent e) {
		String user = suggestBox.getValue();
		addUser(user);
		suggestBox.setValue("");
	}

	@UiHandler("suggestBox")
	public void onKeyPress(KeyPressEvent e) {
		char key = e.getCharCode();
		if (key != '\r' && key != '\n' && key != ',')
			return;
		String user = suggestBox.getValue();
		addUser(user);
		suggestBox.setValue("");
		e.preventDefault();

		if (key != '\r' && key != '\n')
			return;
		submitUsers();
	}

	private void removeLast() {
		int widgetCount = flowPanel.getWidgetCount();
		if (widgetCount < 2)
			return;
		int widgetPos = widgetCount - 2;
		Widget widget = flowPanel.getWidget(widgetPos);
		assert(widget instanceof AddedParticipantWidget);
		AddedParticipantWidget participantWidget = (AddedParticipantWidget) widget;
		String text = participantWidget.getText();
		users.remove(text);
		flowPanel.remove(widget);
	}

	private void submitUsers() {
		presenter.addedUsers(users.values());
	}

	public boolean isEmailOnList() {
		Collection<String> usersCollection = users.values();
		for (String user : usersCollection) {
			boolean isEmail = user
					.matches("^[A-z0-9._%+-]+@[A-z0-9.-]+\\.[A-z]{2,4}$");
			if (isEmail)
				return true;
		}
		return false;
	}

	public void setVisibility() {
		if (users.size() == 0) {
			saveButton.setEnabled(false);
		} else {
			saveButton.setEnabled(true);
		}
		if (!isEmailOnList()) {
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
		if (source instanceof AddedParticipantWidget) {
			AddedParticipantWidget addedWidget = (AddedParticipantWidget) source;
			String userName = addedWidget.getText();
			users.remove(userName);
			flowPanel.remove(addedWidget);
			setVisibility();
		}
	}

	@UiHandler("saveButton")
	public void onSave(ClickEvent e) {
		submitUsers();
	}

	@UiHandler("cancelButton")
	public void onCancel(ClickEvent e) {
		presenter.cancel();
	}

	@Override
	public void setUsersList(List<String> users) {
		oracle.clear();
		oracle.add("marek");
		oracle.add("jacek");
		oracle.add("janek");
		for (String user : users) {
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

	@Override
	public Widget asWidget() {
		suggestBox.setFocus(true);
		for (int i = 0; i < flowPanel.getWidgetCount();) {
			Widget widget = flowPanel.getWidget(i);
			if (widget instanceof AddedParticipantWidget) {
				flowPanel.remove(i);
			} else {
				++i;
			}
		}
		users.clear();
		setVisibility();
		return super.asWidget();
	}

}
