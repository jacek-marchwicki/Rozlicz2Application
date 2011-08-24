package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.rozlicz2.application.shared.ProjectEntity;

public interface ProjectsView extends IsWidget {
	void setUserName(String userName);
	void setProjectsNumber(int numberOfProjects);
	void setProjectsList(List<ProjectEntity> projectsDetails);
	public interface Presenter {
		void createProject();
	}
	void setPresenter(Presenter presenter);
}
