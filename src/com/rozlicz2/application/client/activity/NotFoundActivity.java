package com.rozlicz2.application.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.rozlicz2.application.client.view.NotFoundView;

public class NotFoundActivity extends AbstractActivity implements Activity {

	private NotFoundView view;

	public NotFoundActivity() {
	}

	@Inject
	public void setView(NotFoundView view) {
		this.view = view;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view.asWidget());
	}

}
