package com.rozlicz2.application.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.rozlicz2.application.client.activity.BreadcrumbActivity;
import com.rozlicz2.application.client.activity.ErrorActivity;
import com.rozlicz2.application.client.mvp.AppActivityMapper;
import com.rozlicz2.application.client.mvp.AppPlaceHistoryMapper;
import com.rozlicz2.application.client.mvp.BreadcrumbActivityMapper;
import com.rozlicz2.application.client.mvp.PopupActivityMapper;
import com.rozlicz2.application.client.place.ProjectsPlace;
import com.rozlicz2.application.client.view.DashboardView;
import com.rozlicz2.application.shared.service.ListwidgetRequestFactory;

public class Starter {
	private final DashboardView dashboard;
	private final Place defaultPlace = new ProjectsPlace();
	private final PlaceHistoryHandler historyHandler;

	@Inject
	public Starter(EventBus eventBus, ListwidgetRequestFactory rf,
			PlaceController placeController,
			PopupActivityMapper popupActivityMapper,
			AppActivityMapper appActivityMapper,
			AppPlaceHistoryMapper historyMapper,
			UncaughtExceptionHandler uncaughtExceptionHandler,
			ErrorActivity errorActivity, DashboardView dashboard,
			BreadcrumbActivity breadcrumbActivity,
			BreadcrumbActivityMapper breadcrumbActivityMapper) {

		this.dashboard = dashboard;
		rf.initialize(eventBus);
		if (GWT.isProdMode())
			GWT.setUncaughtExceptionHandler(uncaughtExceptionHandler);

		ActivityManager popupActivityManager = new ActivityManager(
				popupActivityMapper, eventBus);

		ActivityManager activityManager = new ActivityManager(
				appActivityMapper, eventBus);
		ActivityManager breadcrumbManager = new ActivityManager(
				breadcrumbActivityMapper, eventBus);
		historyHandler = new PlaceHistoryHandler(historyMapper);
		historyHandler.register(placeController, eventBus, defaultPlace);

		popupActivityManager.setDisplay(dashboard.getPopupWidget());
		activityManager.setDisplay(dashboard.getAppWidget());
		breadcrumbManager.setDisplay(dashboard.getBreadcrumbPanel());
		errorActivity.start(dashboard.getErrorPanel(), null);
	}

	public void start(RootPanel rootPanel) {
		rootPanel.add(dashboard);
		historyHandler.handleCurrentHistory();
	}
}
