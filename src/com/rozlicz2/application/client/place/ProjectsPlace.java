package com.rozlicz2.application.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class ProjectsPlace extends Place {
	public static class Tokenizer implements PlaceTokenizer<ProjectsPlace> {

		@Override
		public ProjectsPlace getPlace(String token) {
			return null;
		}

		@Override
		public String getToken(ProjectsPlace place) {
			return null;
		}

	}
}
