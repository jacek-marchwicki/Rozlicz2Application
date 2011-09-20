package com.rozlicz2.application.client.activity;

import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.ResettableEventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.rozlicz2.application.client.ClientFactory;
import com.rozlicz2.application.client.event.ProjectsListEvent;
import com.rozlicz2.application.client.place.ProjectPlace;
import com.rozlicz2.application.client.place.ProjectsPlace;
import com.rozlicz2.application.client.resources.ApplicationConstants;
import com.rozlicz2.application.client.view.ProjectsView;
import com.rozlicz2.application.shared.proxy.ProjectListProxy;
import com.rozlicz2.application.shared.proxy.ProjectProxy;
import com.rozlicz2.application.shared.service.ListwidgetRequestFactory;
import com.rozlicz2.application.shared.service.ListwidgetRequestFactory.ProjectListRequestContext;
import com.rozlicz2.application.shared.service.ListwidgetRequestFactory.ProjectRequestContext;
import com.rozlicz2.application.shared.tools.IdGenerator;

public class ProjectsActivity extends AbstractActivity implements
		ProjectsView.Presenter {

	private ResettableEventBus childEventBus;
	private final ClientFactory clientFactory;
	private ProjectsView projectsView;
	private final ListwidgetRequestFactory rf;

	public ProjectsActivity(ProjectsPlace place, ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
		rf = clientFactory.getRf();
	}

	@Override
	public void createProject() {
		ProjectRequestContext projectRequest = rf.getProjectRequest();
		ProjectProxy project = projectRequest.create(ProjectProxy.class);
		project.setName(ApplicationConstants.constants.newProject());
		project.setId(IdGenerator.nextId());
		projectRequest.save(project).fire(new Receiver<Void>() {

			@Override
			public void onFailure(ServerFailure error) {
				Window.alert("error: " + error);
				super.onFailure(error);
			}

			@Override
			public void onSuccess(Void response) {
				Window.alert("saved");
			}
		});
		ProjectPlace place = new ProjectPlace(project);
		clientFactory.getPlaceController().goTo(place);
	}

	@Override
	public void editProject(ProjectListProxy key) {
		ProjectPlace place = new ProjectPlace(key.getId());
		clientFactory.getPlaceController().goTo(place);
	}

	@Override
	public void onCancel() {
		// maybe remove handlers?
	}

	@Override
	public void onStop() {
		childEventBus.removeHandlers();
	}

	protected void populateProjectsList(
			List<ProjectListProxy> readOnlyProjectsList) {
		projectsView.setProjectsNumber(readOnlyProjectsList.size());
		projectsView.setProjectsList(readOnlyProjectsList);
	}

	private void receiveData(final EventBus eventBus) {
		ProjectListRequestContext projectListRequest = rf.getProjectListRequest();
		Request<List<ProjectListProxy>> listAll = projectListRequest.listAll();
		listAll.fire(new Receiver<List<ProjectListProxy>>() {

			@Override
			public void onFailure(ServerFailure error) {
				Window.alert("error: " + error);
				super.onFailure(error);
			}

			@Override
			public void onSuccess(List<ProjectListProxy> response) {
				ProjectsListEvent event = new ProjectsListEvent(response);
				eventBus.fireEvent(event);
			}
		});
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.childEventBus = new ResettableEventBus(eventBus);
		childEventBus.addHandler(ProjectsListEvent.TYPE,
				new ProjectsListEvent.Handler() {

					@Override
					public void onProjectsList(ProjectsListEvent event) {
						populateProjectsList(event.getReadOnlyProjectsList());
					}
				});

		projectsView = clientFactory.getProjectsView();
		projectsView.setPresenter(this);

		receiveData(eventBus);
		panel.setWidget(projectsView.asWidget());
	}

}
