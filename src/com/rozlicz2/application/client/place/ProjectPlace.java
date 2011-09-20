package com.rozlicz2.application.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.rozlicz2.application.shared.proxy.ProjectProxy;

public class ProjectPlace extends Place {
	@Prefix("project")
	public static class Tokenizer implements PlaceTokenizer<ProjectPlace> {

		@Override
		public ProjectPlace getPlace(String token) {
			return new ProjectPlace(token);
		}

		@Override
		public String getToken(ProjectPlace place) {
			return place.projectId;
		}

	}

	private ProjectProxy project = null;
	private String projectId;

	public ProjectPlace(ProjectProxy project) {
		this.setProject(project);
		projectId = project.getId();
	}

	public ProjectPlace(String projectId) {
		this.projectId = projectId;
	}

	public ProjectProxy getProject() {
		return project;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProject(ProjectProxy project) {
		this.project = project;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

}
