package com.rozlicz2.application.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.ResettableEventBus;
import com.rozlicz2.application.client.event.ProjectChangedEvent;
import com.rozlicz2.application.client.place.ExpensePlace;
import com.rozlicz2.application.client.place.ProjectPlace;
import com.rozlicz2.application.client.place.ProjectsPlace;
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
						view.changeLastBreadcrumbItemName(event.getProject()
								.getName());
					}
				});

		if (place instanceof ProjectPlace) {
			view.addBreadcrumbItem("Projects");
			view.addBreadcrumbItem("Project");
		} else if (place instanceof ProjectsPlace) {
			view.addBreadcrumbItem("Projects");
		} else if (place instanceof ExpensePlace) {
			view.addBreadcrumbItem("Projects");
			view.addBreadcrumbItem("Project");
			// TODO change Project into it's name
			view.addBreadcrumbItem("Expense");
			// TODO Handler for expenseChanged
		} else {
			view.addBreadcrumbItem("Please define me in breadcrumbActivity.java!");

		}
		panel.setWidget(view);

	}
}
