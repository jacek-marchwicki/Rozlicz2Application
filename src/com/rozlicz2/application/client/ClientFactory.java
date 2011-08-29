package com.rozlicz2.application.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;
import com.rozlicz2.application.client.dao.ExpensesDAO;
import com.rozlicz2.application.client.dao.ProjectsDAO;
import com.rozlicz2.application.client.dao.ProjectsShortDAO;
import com.rozlicz2.application.client.view.ExpenseView;
import com.rozlicz2.application.client.view.NotFoundView;
import com.rozlicz2.application.client.view.ProjectView;
import com.rozlicz2.application.client.view.ProjectsView;

public interface ClientFactory {
	EventBus getEventBus();
    PlaceController getPlaceController();
    ProjectsView getProjectsView();
    ProjectsShortDAO getProjectsShortDAO();
	ProjectView getProjectView();
	ProjectsDAO getProjectsDAO();
	ExpensesDAO getExpensesDAO();
	NotFoundView  getNotFoundView();
	ExpenseView getExpenseView();
}
