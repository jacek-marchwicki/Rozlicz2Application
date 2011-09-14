package com.rozlicz2.application.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.rozlicz2.application.client.dao.AbstractEntityProvider;
import com.rozlicz2.application.client.dao.SyncKey;

public interface ProjectsView extends IsWidget {
	public interface Presenter {
		void createProject();

		void editProject(SyncKey key);
	}

	void setPresenter(Presenter presenter);

	void setProjectsList(AbstractEntityProvider dataProvider);

	void setProjectsNumber(int numberOfProjects);

	void setUserName(String userName);
}
