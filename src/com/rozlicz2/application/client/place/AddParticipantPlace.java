package com.rozlicz2.application.client.place;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.rozlicz2.application.client.mvp.AppPlaceHistoryMapper;

public class AddParticipantPlace extends Place {
	@Prefix("addParticipant")
	public static class Tokenizer implements
			PlaceTokenizer<AddParticipantPlace> {
		AppPlaceHistoryMapper historyMapper = GWT
				.create(AppPlaceHistoryMapper.class);

		@Override
		public AddParticipantPlace getPlace(String token) {

			int lastIndexOf = token.lastIndexOf(':');
			String previousPlaceToken = token.substring(0, lastIndexOf);
			if (lastIndexOf >= token.length())
				return null;
			String projectId = token.substring(lastIndexOf + 1);

			Place previousPlace = historyMapper.getPlace(previousPlaceToken);
			return new AddParticipantPlace(previousPlace, projectId);
		}

		@Override
		public String getToken(AddParticipantPlace place) {

			String token = historyMapper.getToken(place.getPreviousPlace());
			return token + ":" + place.getProjectId();
		}

	}

	private final Place previousPlace;
	private final String projectId;

	public AddParticipantPlace(Place previousPlace, String projectId) {
		this.previousPlace = previousPlace;
		this.projectId = projectId;
	}

	public Place getPreviousPlace() {
		return this.previousPlace;
	}

	public String getProjectId() {
		return projectId;
	}

}
