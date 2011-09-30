package com.rozlicz2.application.client.activity;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.ResettableEventBus;
import com.rozlicz2.application.client.mvp.UncaughtExceptionEvent;
import com.rozlicz2.application.client.view.ErrorView;

public class ErrorActivity extends AbstractActivity {

	private static final int ERROR_TIMEOUT = 5000;
	private final Set<ErrorView.ErrorDisplay> errors = new HashSet<ErrorView.ErrorDisplay>();
	private ErrorView errorView;

	private EventBus eventBus;

	private ResettableEventBus resettableEventBus;

	protected void dispalyNewError(UncaughtExceptionEvent event) {
		final ErrorView.ErrorDisplay errorDisplay = new ErrorView.ErrorDisplay();
		errorDisplay.setErrorInfo(event.getThrowable().getMessage());
		errors.add(errorDisplay);
		Timer t = new Timer() {
			@Override
			public void run() {
				errors.remove(errorDisplay);
				refreshView();
			}
		};
		t.schedule(ERROR_TIMEOUT);
		refreshView();
	}

	protected void refreshView() {
		errorView.setErrors(errors);

	}

	@Inject
	public void setErrorView(ErrorView errorView) {
		this.errorView = errorView;
	}

	@Inject
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public void start(AcceptsOneWidget panel,
			com.google.gwt.event.shared.EventBus e) {
		resettableEventBus = new ResettableEventBus(eventBus);
		resettableEventBus.addHandler(UncaughtExceptionEvent.TYPE,
				new UncaughtExceptionEvent.Handler() {

					@Override
					public void onUncaughtException(UncaughtExceptionEvent event) {
						dispalyNewError(event);
					}
				});
		panel.setWidget(errorView);
	}

}
