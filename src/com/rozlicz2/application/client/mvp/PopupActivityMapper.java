package com.rozlicz2.application.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.rozlicz2.application.client.ClientFactory;
import com.rozlicz2.application.client.activity.AddParticipantActivity;
import com.rozlicz2.application.client.place.AddParticipantPlace;

public class PopupActivityMapper implements ActivityMapper {

	private final ClientFactory clientFactory;

	public PopupActivityMapper(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public Activity getActivity(Place place) {
		if (place instanceof AddParticipantPlace)
			return new AddParticipantActivity((AddParticipantPlace) place,
					clientFactory);
		return null;
	}

}
