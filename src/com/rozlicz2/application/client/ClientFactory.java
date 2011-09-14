package com.rozlicz2.application.client;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.rozlicz2.application.client.dao.SyncDatastoreService;
import com.rozlicz2.application.client.view.AddParticipantView;
import com.rozlicz2.application.client.view.ExpenseView;
import com.rozlicz2.application.client.view.NotFoundView;
import com.rozlicz2.application.client.view.ProjectView;
import com.rozlicz2.application.client.view.ProjectsView;

public interface ClientFactory {
	AddParticipantView getAddParticipantView();

	SyncDatastoreService getDAO();

	EventBus getEventBus();

	ExpenseView getExpenseView();

	NotFoundView getNotFoundView();

	PlaceController getPlaceController();

	ProjectsView getProjectsView();

	ProjectView getProjectView();
}
