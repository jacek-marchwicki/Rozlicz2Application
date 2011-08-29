package com.rozlicz2.application.client.activity;

import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.rozlicz2.application.client.ClientFactory;
import com.rozlicz2.application.client.entity.ExpenseEntity;
import com.rozlicz2.application.client.entity.ExpenseShortEntity;
import com.rozlicz2.application.client.entity.ProjectEntity;
import com.rozlicz2.application.client.place.ExpensePlace;
import com.rozlicz2.application.client.place.NotFoundPlace;
import com.rozlicz2.application.client.place.ProjectPlace;
import com.rozlicz2.application.client.view.ProjectView;

public class ProjectActivity extends AbstractActivity implements ProjectView.Presenter {

	private ClientFactory clientFactory;
	private final ProjectPlace place;
	private ProjectEntity project;

	public ProjectActivity(ProjectPlace place, ClientFactory clientFactory) {
		this.place = place;
		this.clientFactory = clientFactory;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		
		project = clientFactory.getProjectsDAO().getProject(place.getProjectId());
		if (project == null) {
			Place place = new NotFoundPlace();
			clientFactory.getPlaceController().goTo(place);
			return;
		}
		List<ExpenseShortEntity> expenses= project.getExpensesShort();
		
		ProjectView projectView = clientFactory.getProjectView();
		projectView.setPresenter(this);
        projectView.setExpenses(expenses);
        projectView.setProjectName(project.getName());
        panel.setWidget(projectView.asWidget());
	}

	@Override
	public void setProjectName(String projectName) {
		project.setName(projectName);
		clientFactory.getProjectsDAO().save(project);
		clientFactory.getProjectsShortDAO().save(project);
	}

	@Override
	public void createExpense() {
		ExpenseEntity expenseEntity = new ExpenseEntity(project.getId());
		
		project.addExpenseShort(expenseEntity);
		clientFactory.getExpensesDAO().addExpense(expenseEntity);
		clientFactory.getProjectsDAO().save(project);
		clientFactory.getProjectsShortDAO().save(project);
		
		ExpensePlace place = new ExpensePlace(expenseEntity.getId());
		clientFactory.getPlaceController().goTo(place);
	}

	@Override
	public void editExpense(Long expenseId) {
		ExpensePlace place = new ExpensePlace(expenseId);
		clientFactory.getPlaceController().goTo(place);
	}
	
	
}
