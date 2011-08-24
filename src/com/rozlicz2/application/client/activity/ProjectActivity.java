package com.rozlicz2.application.client.activity;

import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.rozlicz2.application.client.ClientFactory;
import com.rozlicz2.application.client.dao.ExpenseShort;
import com.rozlicz2.application.client.dao.Project;
import com.rozlicz2.application.client.place.ProjectPlace;
import com.rozlicz2.application.client.view.ProjectView;

public class ProjectActivity extends AbstractActivity {

	private ClientFactory clientFactory;
	private final ProjectPlace place;

	public ProjectActivity(ProjectPlace place, ClientFactory clientFactory) {
		this.place = place;
		this.clientFactory = clientFactory;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		ProjectView projectView = clientFactory.getProjectView();
		Project project = clientFactory.getProjectDAO().getProject(place.getProjectId());
		List<ExpenseShort> expenses= project.getExpensesShort();
        projectView.setExpenses(expenses);
        projectView.setProjectName(project.getName());
        panel.setWidget(projectView.asWidget());
	}

}
