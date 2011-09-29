package com.rozlicz2.application.client.mvp;

import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

class AppUncaughtExceptionHandler implements UncaughtExceptionHandler {
	private EventBus eventBus;

	@Override
	public void onUncaughtException(Throwable e) {
		UncaughtExceptionEvent exception = new UncaughtExceptionEvent(e);
		eventBus.fireEvent(exception);
	}

	@Inject
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}
}