package com.rozlicz2.application.client;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.rozlicz2.application.client.dao.SyncDatastoreService;
import com.rozlicz2.application.client.dao.SyncDatastoreServiceFactory;
import com.rozlicz2.application.client.view.AddParticipantView;
import com.rozlicz2.application.client.view.AddParticipantWidget;
import com.rozlicz2.application.client.view.ExpenseView;
import com.rozlicz2.application.client.view.ExpenseViewImpl;
import com.rozlicz2.application.client.view.NotFoundView;
import com.rozlicz2.application.client.view.NotFoundViewImpl;
import com.rozlicz2.application.client.view.ProjectView;
import com.rozlicz2.application.client.view.ProjectViewImpl;
import com.rozlicz2.application.client.view.ProjectsView;
import com.rozlicz2.application.client.view.ProjectsViewImpl;

public class ClientFactoryImpl implements ClientFactory {
	private final SyncDatastoreService dao = SyncDatastoreServiceFactory
			.getDatastoreService();
	private final EventBus eventBus = new SimpleEventBus();
	private final ExpenseView expenseView = new ExpenseViewImpl();
	private final NotFoundView notFoundView = new NotFoundViewImpl();
	private final AddParticipantView participanView = new AddParticipantWidget();
	private final PlaceController placeController = new PlaceController(
			eventBus);
	private final ProjectsView projectsView = new ProjectsViewImpl();
	private final ProjectView projectView = new ProjectViewImpl();

	@Override
	public AddParticipantView getAddParticipantView() {
		return participanView;
	}

	@Override
	public SyncDatastoreService getDAO() {
		return dao;
	}

	@Override
	public EventBus getEventBus() {
		return eventBus;
	}

	@Override
	public ExpenseView getExpenseView() {
		return expenseView;
	}

	@Override
	public NotFoundView getNotFoundView() {
		return notFoundView;
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
	public ProjectView getProjectView() {
		return projectView;
	}

}
