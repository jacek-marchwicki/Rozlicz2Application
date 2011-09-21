package com.rozlicz2.application.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

public class ProjectsPlace extends Place {
	@Prefix("projects")
	public static class Tokenizer implements PlaceTokenizer<ProjectsPlace> {

		@Override
		public ProjectsPlace getPlace(String token) {
			return new ProjectsPlace();
		}

		@Override
		public String getToken(ProjectsPlace place) {
			return "";
		}

	}
}
