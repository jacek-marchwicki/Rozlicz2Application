package com.rozlicz2.application.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.rozlicz2.application.client.activity.ExpenseActivity;
import com.rozlicz2.application.client.activity.NotFoundActivity;
import com.rozlicz2.application.client.activity.ProjectActivity;
import com.rozlicz2.application.client.activity.ProjectsActivity;
import com.rozlicz2.application.client.place.ExpensePlace;
import com.rozlicz2.application.client.place.NotFoundPlace;
import com.rozlicz2.application.client.place.ProjectPlace;
import com.rozlicz2.application.client.place.ProjectsPlace;

public class AppActivityMapper implements ActivityMapper {

	private Provider<ExpenseActivity> expenseActivityProvider;
	private Provider<NotFoundActivity> notFoundActivityProvider;
	private Provider<ProjectActivity> projectActivityProvider;
	private Provider<ProjectsActivity> projectsActivityProvider;

	public AppActivityMapper() {
	}

	@Override
	public Activity getActivity(Place place) {
		if (place instanceof ProjectsPlace) {
			ProjectsActivity projectsActivity = projectsActivityProvider.get();
			return projectsActivity;
		} else if (place instanceof ProjectPlace) {
			ProjectActivity projectActivity = projectActivityProvider.get();
			projectActivity.setPlace((ProjectPlace) place);
			return projectActivity;
		} else if (place instanceof NotFoundPlace) {
			NotFoundActivity notFoundActivity = notFoundActivityProvider.get();
			return notFoundActivity;
		} else if (place instanceof ExpensePlace) {
			ExpenseActivity expenseActivity = expenseActivityProvider.get();
			expenseActivity.setPlace((ExpensePlace) place);
			return expenseActivity;
		}
		return null;
	}

	@Inject
	public void setExpenseActivityProvider(
			Provider<ExpenseActivity> expenseActivityProvider) {
		this.expenseActivityProvider = expenseActivityProvider;
	}

	@Inject
	public void setNotFoundActivityProvider(
			Provider<NotFoundActivity> notFoundActivityProvider) {
		this.notFoundActivityProvider = notFoundActivityProvider;
	}

	@Inject
	public void setProjectActivityProvider(
			Provider<ProjectActivity> projectActivityProvider) {
		this.projectActivityProvider = projectActivityProvider;
	}

	@Inject
	public void setProjectsActivityProvider(
			Provider<ProjectsActivity> projectsActivityProvider) {
		this.projectsActivityProvider = projectsActivityProvider;
	}

}
