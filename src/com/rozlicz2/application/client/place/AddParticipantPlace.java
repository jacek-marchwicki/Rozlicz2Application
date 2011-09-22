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
			Place previousPlace = historyMapper.getPlace(token);
			return new AddParticipantPlace(previousPlace);
		}

		@Override
		public String getToken(AddParticipantPlace place) {

			String token = historyMapper.getToken(place.getPreviousPlace());
			return token;
		}

	}

	private final Place previousPlace;

	public AddParticipantPlace(Place previousPlace) {
		this.previousPlace = previousPlace;
	}

	public Place getPreviousPlace() {
		return this.previousPlace;
	}

}
