package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.rozlicz2.application.shared.proxy.ProjectListProxy;

public interface ProjectsView extends IsWidget {
	public interface Presenter {
		void createProject();

		void editProject(ProjectListProxy key);

		void generateError();
	}

	void setLocked(boolean locked);

	void setPresenter(Presenter presenter);

	void setProjectsList(List<ProjectListProxy> list);

	void setProjectsNumber(int numberOfProjects);
}
