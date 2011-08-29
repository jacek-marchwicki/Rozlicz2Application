package com.rozlicz2.application.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class NotFoundPlace extends Place {
	public static class Tokenizer implements PlaceTokenizer<NotFoundPlace> {

		@Override
		public NotFoundPlace getPlace(String token) {
			return null;
		}

		@Override
		public String getToken(NotFoundPlace place) {
			return null;
		}

	}
}
