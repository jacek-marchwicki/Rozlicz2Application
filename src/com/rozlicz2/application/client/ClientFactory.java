package com.rozlicz2.application.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.rozlicz2.application.client.dao.ProjectDAO;
import com.rozlicz2.application.client.dao.ProjectsDAO;
import com.rozlicz2.application.client.view.ProjectView;
import com.rozlicz2.application.client.view.ProjectsView;

public interface ClientFactory {
	EventBus getEventBus();
    PlaceController getPlaceController();
    ProjectsView getProjectsView();
    ProjectsDAO getProjectsDAO();
	ProjectView getProjectView();
	ProjectDAO getProjectDAO();
}
