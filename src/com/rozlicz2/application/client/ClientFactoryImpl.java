package com.rozlicz2.application.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.rozlicz2.application.client.dao.ExpensesDAO;
import com.rozlicz2.application.client.dao.ExpensesDAOImpl;
import com.rozlicz2.application.client.dao.ProjectsDAO;
import com.rozlicz2.application.client.dao.ProjectsDAOImpl;
import com.rozlicz2.application.client.dao.ProjectsShortDAO;
import com.rozlicz2.application.client.dao.ProjectsShortDAOImpl;
import com.rozlicz2.application.client.view.ExpenseView;
import com.rozlicz2.application.client.view.ExpenseViewImpl;
import com.rozlicz2.application.client.view.NotFoundView;
import com.rozlicz2.application.client.view.NotFoundViewImpl;
import com.rozlicz2.application.client.view.ProjectView;
import com.rozlicz2.application.client.view.ProjectViewImpl;
import com.rozlicz2.application.client.view.ProjectsView;
import com.rozlicz2.application.client.view.ProjectsViewImpl;

public class ClientFactoryImpl implements ClientFactory {
	private final EventBus eventBus = new SimpleEventBus();
    private final PlaceController placeController = new PlaceController(eventBus);
    private final ProjectsView projectsView = new ProjectsViewImpl();
	private ProjectsShortDAO projectsShortDAO = new ProjectsShortDAOImpl();
	private ProjectView projectView = new ProjectViewImpl();
	private ProjectsDAO projectsDAO = new ProjectsDAOImpl();
	private ExpensesDAO expensesDAO = new ExpensesDAOImpl();
	private NotFoundView notFoundView = new NotFoundViewImpl();
	private ExpenseView expenseView = new ExpenseViewImpl();
    
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
	public ProjectsShortDAO getProjectsShortDAO() {
		return projectsShortDAO ;
	}

	@Override
	public ProjectView getProjectView() {
		return projectView ;
	}

	@Override
	public ProjectsDAO getProjectsDAO() {
		return projectsDAO ;
	}

	@Override
	public ExpensesDAO getExpensesDAO() {
		return expensesDAO;
	}

	@Override
	public NotFoundView getNotFoundView() {
		return notFoundView ;
	}

	@Override
	public ExpenseView getExpenseView() {
		return expenseView;
	}

}
