package com.rozlicz2.application.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.rozlicz2.application.client.ClientFactory;
import com.rozlicz2.application.client.place.NotFoundPlace;
import com.rozlicz2.application.client.view.NotFoundView;

public class NotFoundActivity extends AbstractActivity implements Activity {

	private ClientFactory clientFactory;

	public NotFoundActivity(NotFoundPlace place, ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		NotFoundView view = clientFactory.getNotFoundView();
		panel.setWidget(view.asWidget());
	}

}
