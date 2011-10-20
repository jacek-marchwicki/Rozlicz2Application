package com.rozlicz2.application.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.ResettableEventBus;
import com.rozlicz2.application.client.event.ExpenseChangedEvent;
import com.rozlicz2.application.client.event.ProjectChangedEvent;
import com.rozlicz2.application.client.place.ExpensePlace;
import com.rozlicz2.application.client.place.ProjectPlace;
import com.rozlicz2.application.client.place.ProjectsPlace;
import com.rozlicz2.application.client.view.BreadcrumbView;

public class BreadcrumbActivity extends AbstractActivity implements
		BreadcrumbView.Presenter {

	private static final String KEY_EXPENSE = "expense_key";
	private static final String KEY_PROJECT = "project_key";
	private static final String KEY_PROJECTS = "projects_key";
	private ResettableEventBus bus;
	private EventBus eventBus;
	private Place place;
	private PlaceController placeController;
	private BreadcrumbView view;

	@Override
	public void gotoKey(String key) {
		if (key.equals(KEY_PROJECTS)) {
			Place newPlace = new ProjectsPlace();
			placeController.goTo(newPlace);
		}
	}

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

	@Inject
	public void setPlaceController(PlaceController placeController) {
		this.placeController = placeController;
	}

	@Override
	public void start(AcceptsOneWidget panel,
			com.google.gwt.event.shared.EventBus e) {
		view.setPresenter(this);
		bus = new ResettableEventBus(eventBus);
		bus.addHandler(ProjectChangedEvent.TYPE,
				new ProjectChangedEvent.Handler() {

					@Override
					public void onProjectChanged(ProjectChangedEvent event) {
						view.changeBreadcrumbItemNameByKey(KEY_PROJECT, event
								.getProject().getName());
					}
				});
		bus.addHandler(ExpenseChangedEvent.TYPE,
				new ExpenseChangedEvent.Handler() {

					@Override
					public void onExpenseChange(
							ExpenseChangedEvent expenseChangedEvent) {
						view.changeBreadcrumbItemNameByKey(KEY_EXPENSE,
								expenseChangedEvent.getReadOnlyExpense()
										.getName());
					}
				});
		view.clearBreadcrumbItems();

		if (place instanceof ProjectPlace) {
			view.addBreadcrumbItem(KEY_PROJECTS, "Projects");
			view.addBreadcrumbItem(KEY_PROJECT, "Project");
		} else if (place instanceof ProjectsPlace) {
			view.addBreadcrumbItem(KEY_PROJECTS, "Projects");
		} else if (place instanceof ExpensePlace) {
			view.addBreadcrumbItem(KEY_PROJECTS, "Projects");
			view.addBreadcrumbItem(KEY_PROJECT, "Project");
			view.addBreadcrumbItem(KEY_EXPENSE, "Expense");
			// TODO Handler for expenseChanged
		} else {
			view.addBreadcrumbItem("key error not found",
					"Please define me in breadcrumbActivity.java!");

		}
		panel.setWidget(view);

	}
}
