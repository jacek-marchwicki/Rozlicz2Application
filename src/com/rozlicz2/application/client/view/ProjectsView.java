package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.rozlicz2.application.client.entity.ProjectShortEntity;

public interface ProjectsView extends IsWidget {
	void setUserName(String userName);
	void setProjectsNumber(int numberOfProjects);
	void setProjectsList(List<ProjectShortEntity> projectsDetails);
	public interface Presenter {
		void createProject();
		void editProject(Long id);
	}
	void setPresenter(Presenter presenter);
}
