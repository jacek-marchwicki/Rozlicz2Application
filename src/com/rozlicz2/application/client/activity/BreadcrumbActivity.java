package com.rozlicz2.application.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.ResettableEventBus;
import com.rozlicz2.application.client.event.ProjectChangedEvent;
import com.rozlicz2.application.client.place.ProjectPlace;
import com.rozlicz2.application.client.view.BreadcrumbView;

public class BreadcrumbActivity extends AbstractActivity {

	private ResettableEventBus bus;
	private EventBus eventBus;
	private Place place;
	private BreadcrumbView view;

	@Override
	public void onStop() {
		bus.removeHandlers();
	}

	@Inject
	public void setBreadcrumbView(BreadcrumbView breadcrumbView) {
		this.view = breadcrumbView;
	}

	@Inject
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	@Override
	public void start(AcceptsOneWidget panel,
			com.google.gwt.event.shared.EventBus e) {
		bus = new ResettableEventBus(eventBus);
		bus.addHandler(ProjectChangedEvent.TYPE,
				new ProjectChangedEvent.Handler() {

					@Override
					public void onProjectChanged(ProjectChangedEvent event) {
						view.setName("xyz:" + event.getProject().getName());
					}
				});
		// TODO Auto-generated method stub
		if (place instanceof ProjectPlace) {
			view.setName("Current project"
					+ ((ProjectPlace) place).getProjectId());
			view.setPreviousLabel("Projects list");
		} else {
			view.setName("Not yet defined");
		}
		panel.setWidget(view);

	}

}
