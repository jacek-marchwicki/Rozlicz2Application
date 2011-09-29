package com.rozlicz2.application.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.rozlicz2.application.client.activity.AddParticipantActivity;
import com.rozlicz2.application.client.place.AddParticipantPlace;

public class PopupActivityMapper implements ActivityMapper {

	private Provider<AddParticipantActivity> addParticipantActivityProvider;

	public PopupActivityMapper() {
	}

	@Override
	public Activity getActivity(Place place) {
		if (place instanceof AddParticipantPlace) {
			AddParticipantActivity addParticipantActivity = addParticipantActivityProvider
					.get();
			addParticipantActivity.setPlace((AddParticipantPlace) place);
			return addParticipantActivity;
		}
		return null;
	}

	@Inject
	public void setAddParticipantActivityProvider(
			Provider<AddParticipantActivity> addParticipantActivityProvider) {
		this.addParticipantActivityProvider = addParticipantActivityProvider;
	}

}
