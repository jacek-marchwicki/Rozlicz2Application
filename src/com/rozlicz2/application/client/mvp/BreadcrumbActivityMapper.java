package com.rozlicz2.application.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.rozlicz2.application.client.activity.BreadcrumbActivity;

public class BreadcrumbActivityMapper implements ActivityMapper {

	private Provider<BreadcrumbActivity> breadcrumbActivityProvider;

	@Override
	public Activity getActivity(Place place) {
		BreadcrumbActivity breadcrumbActivity = breadcrumbActivityProvider
				.get();
		breadcrumbActivity.setPlace(place);
		return breadcrumbActivity;
	}

	@Inject
	public void setBreadcrumbActivityProvider(
			Provider<BreadcrumbActivity> breadcrumbActivityProvider) {
		this.breadcrumbActivityProvider = breadcrumbActivityProvider;
	}

}
