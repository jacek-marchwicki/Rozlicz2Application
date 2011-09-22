package com.rozlicz2.application.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.rozlicz2.application.client.mvp.AppActivityMapper;
import com.rozlicz2.application.client.mvp.AppPlaceHistoryMapper;
import com.rozlicz2.application.client.mvp.PopupActivityMapper;
import com.rozlicz2.application.client.place.ProjectsPlace;
import com.rozlicz2.application.shared.service.ListwidgetRequestFactory;

public class Dashboard extends Composite {

	interface DashboardUiBinder extends UiBinder<Widget, Dashboard> {
	}

	private static DashboardUiBinder uiBinder = GWT
			.create(DashboardUiBinder.class);

	public static native String getUserEmailFromHtml() /*-{
														return $wnd.userEmail;
														}-*/;

	@UiField
	SimplePanel appWidget;

	private final Place defaultPlace = new ProjectsPlace();

	@UiField
	Label emailLabel;

	@UiField
	SimplePanel popupWidget;

	public Dashboard() {
		initWidget(uiBinder.createAndBindUi(this));

		String email = getUserEmailFromHtml();
		assert email != null;
		emailLabel.setText(email);

		ClientFactory clientFactory = GWT.create(ClientFactory.class);
		EventBus eventBus = clientFactory.getEventBus();
		ListwidgetRequestFactory rf = clientFactory.getRf();
		rf.initialize(eventBus);
		PlaceController placeController = clientFactory.getPlaceController();

		ActivityMapper popupActivityMapper = new PopupActivityMapper(
				clientFactory);
		ActivityManager popupActivityManager = new ActivityManager(
				popupActivityMapper, eventBus);
		popupActivityManager.setDisplay(popupWidget);
		// Start ActivityManager for the main widget with our ActivityMapper
		ActivityMapper activityMapper = new AppActivityMapper(clientFactory);

		ActivityManager activityManager = new ActivityManager(activityMapper,
				eventBus);
		activityManager.setDisplay(appWidget);

		// Start PlaceHistoryHandler with our PlaceHistoryMapper
		AppPlaceHistoryMapper historyMapper = GWT
				.create(AppPlaceHistoryMapper.class);
		PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(
				historyMapper);
		historyHandler.register(placeController, eventBus, defaultPlace);

		// Goes to the place represented on URL else default place
		historyHandler.handleCurrentHistory();
	}

}
