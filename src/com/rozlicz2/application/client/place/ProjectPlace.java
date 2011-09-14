package com.rozlicz2.application.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class ProjectPlace extends Place {
	public static class Tokenizer implements PlaceTokenizer<ProjectPlace> {

		@Override
		public ProjectPlace getPlace(String token) {
			return new ProjectPlace(Long.parseLong(token));
		}

		@Override
		public String getToken(ProjectPlace place) {
			return Long.toString(place.projectId);
		}

	}

	private long projectId;

	public ProjectPlace(long syncKey) {
		this.projectId = syncKey;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectKey) {
		this.projectId = projectKey;
	}

}
