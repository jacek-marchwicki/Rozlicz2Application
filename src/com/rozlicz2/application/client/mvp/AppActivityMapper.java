package com.rozlicz2.application.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.rozlicz2.application.client.ClientFactory;
import com.rozlicz2.application.client.activity.ExpenseActivity;
import com.rozlicz2.application.client.activity.NotFoundActivity;
import com.rozlicz2.application.client.activity.ProjectActivity;
import com.rozlicz2.application.client.activity.ProjectsActivity;
import com.rozlicz2.application.client.place.ExpensePlace;
import com.rozlicz2.application.client.place.NotFoundPlace;
import com.rozlicz2.application.client.place.ProjectPlace;
import com.rozlicz2.application.client.place.ProjectsPlace;

public class AppActivityMapper implements ActivityMapper {

	private final ClientFactory clientFactory;
	public AppActivityMapper(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
	
	@Override
	public Activity getActivity(Place place) {
		if (place instanceof ProjectsPlace)
            return new ProjectsActivity((ProjectsPlace) place, clientFactory);
		else if (place instanceof ProjectPlace)
			return new ProjectActivity((ProjectPlace) place, clientFactory);
		else if (place instanceof NotFoundPlace )
			return new NotFoundActivity((NotFoundPlace) place, clientFactory);
		else if (place instanceof ExpensePlace)
			return new ExpenseActivity((ExpensePlace) place, clientFactory);
		return null;
	}

}
