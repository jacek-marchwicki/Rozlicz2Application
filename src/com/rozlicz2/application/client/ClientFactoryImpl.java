package com.rozlicz2.application.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.rozlicz2.application.client.dao.ProjectDAO;
import com.rozlicz2.application.client.dao.ProjectDAOImpl;
import com.rozlicz2.application.client.dao.ProjectsDAO;
import com.rozlicz2.application.client.dao.ProjectsDAOImpl;
import com.rozlicz2.application.client.view.ProjectView;
import com.rozlicz2.application.client.view.ProjectViewImpl;
import com.rozlicz2.application.client.view.ProjectsView;
import com.rozlicz2.application.client.view.ProjectsViewImpl;

public class ClientFactoryImpl implements ClientFactory {
	private final EventBus eventBus = new SimpleEventBus();
    private final PlaceController placeController = new PlaceController(eventBus);
    private final ProjectsView projectsView = new ProjectsViewImpl();
	private ProjectsDAO projectsDAO = new ProjectsDAOImpl();
	private ProjectView projectView = new ProjectViewImpl();
	private ProjectDAO projectDAO = new ProjectDAOImpl();
    
	@Override
	public EventBus getEventBus() {
		return eventBus;
	}

	@Override
	public PlaceController getPlaceController() {
		return placeController;
	}

	@Override
	public ProjectsView getProjectsView() {
		return projectsView;
	}

	@Override
	public ProjectsDAO getProjectsDAO() {
		return projectsDAO ;
	}

	@Override
	public ProjectView getProjectView() {
		return projectView ;
	}

	@Override
	public ProjectDAO getProjectDAO() {
		return projectDAO ;
	}

}
