package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.rozlicz2.application.shared.ProjectEntity;

public interface ProjectsView extends IsWidget {
	void setUserName(String userName);
	void setProjectsList(List<ProjectEntity> projectsDetails);
	public interface Presenter {
		void goTo(Place place);
	}
	void setPresenter(Presenter presenter);
}
