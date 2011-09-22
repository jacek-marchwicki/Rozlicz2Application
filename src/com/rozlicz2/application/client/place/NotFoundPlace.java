package com.rozlicz2.application.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class NotFoundPlace extends Place {
	@Prefix("notFound")
	public static class Tokenizer implements PlaceTokenizer<NotFoundPlace> {

		@Override
		public NotFoundPlace getPlace(String token) {
			return null;
		}

		@Override
		public String getToken(NotFoundPlace place) {
			return "";
		}

	}
}
