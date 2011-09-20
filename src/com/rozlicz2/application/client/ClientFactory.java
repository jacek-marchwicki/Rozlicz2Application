package com.rozlicz2.application.client;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.rozlicz2.application.client.view.AddParticipantView;
import com.rozlicz2.application.client.view.ExpenseView;
import com.rozlicz2.application.client.view.NotFoundView;
import com.rozlicz2.application.client.view.ProjectView;
import com.rozlicz2.application.client.view.ProjectsView;
import com.rozlicz2.application.shared.service.ListwidgetRequestFactory;

public interface ClientFactory {
	AddParticipantView getAddParticipantView();

	EventBus getEventBus();

	ExpenseView getExpenseView();

	NotFoundView getNotFoundView();

	PlaceController getPlaceController();

	ProjectsView getProjectsView();

	ProjectView getProjectView();

	ListwidgetRequestFactory getRf();
}
