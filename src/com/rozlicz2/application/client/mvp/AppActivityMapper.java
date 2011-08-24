package com.rozlicz2.application.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.rozlicz2.application.client.ClientFactory;
import com.rozlicz2.application.client.activity.ProjectActivity;
import com.rozlicz2.application.client.activity.ProjectsActivity;
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
		return null;
	}

}
