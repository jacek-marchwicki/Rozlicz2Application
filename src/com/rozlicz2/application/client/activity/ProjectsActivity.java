package com.rozlicz2.application.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.rozlicz2.application.client.ClientFactory;
import com.rozlicz2.application.client.place.ProjectsPlace;
import com.rozlicz2.application.client.view.ProjectsView;

public class ProjectsActivity extends AbstractActivity implements ProjectsView.Presenter {

	private final ClientFactory clientFactory;

	public ProjectsActivity(ProjectsPlace place, ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		ProjectsView projectsView = clientFactory.getProjectsView();
        projectsView.setPresenter(this);
        projectsView.setProjectsList(clientFactory.getProjectsDAO().getAll());
        projectsView.setProjectsNumber(clientFactory.getProjectsDAO().getCount());
        panel.setWidget(projectsView.asWidget());

	}

	@Override
	public void goTo(Place place) {
		clientFactory.getPlaceController().goTo(place);
	}

}
