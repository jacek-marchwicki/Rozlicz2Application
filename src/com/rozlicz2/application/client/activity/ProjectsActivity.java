package com.rozlicz2.application.client.activity;

import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.ResettableEventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.rozlicz2.application.client.event.ProjectsListEvent;
import com.rozlicz2.application.client.mvp.UncaughtExceptionEvent;
import com.rozlicz2.application.client.place.ProjectPlace;
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
	private EventBus eventBus;
	private PlaceController placeController;
	private ProjectsView projectsView;
	private ListwidgetRequestFactory rf;

	public ProjectsActivity() {
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
		placeController.goTo(place);
	}

	@Override
	public void editProject(ProjectListProxy key) {
		ProjectPlace place = new ProjectPlace(key.getId());
		placeController.goTo(place);
	}

	@Override
	public void generateError() {
		// TODO to remove
		Throwable e = new Throwable("this is some error");
		UncaughtExceptionEvent exception = new UncaughtExceptionEvent(e);
		eventBus.fireEvent(exception);
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
		projectsView.setLocked(false);
		projectsView.setProjectsNumber(readOnlyProjectsList.size());
		projectsView.setProjectsList(readOnlyProjectsList);
	}

	private void receiveData() {
		ProjectListRequestContext projectListRequest = rf
				.getProjectListRequest();
		Request<List<ProjectListProxy>> listAll = projectListRequest.listAll();

		listAll.fire(new Receiver<List<ProjectListProxy>>() {

			@Override
			public void onSuccess(List<ProjectListProxy> response) {
				ProjectsListEvent event = new ProjectsListEvent(response);
				eventBus.fireEvent(event);
			}
		});

	}

	@Inject
	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Inject
	public void setPlaceController(PlaceController placeController) {
		this.placeController = placeController;
	}

	@Inject
	public void setProjectsView(ProjectsView projectsView) {
		this.projectsView = projectsView;
	}

	@Inject
	public void setRf(ListwidgetRequestFactory rf) {
		this.rf = rf;
	}

	@Override
	public void start(AcceptsOneWidget panel,
			com.google.gwt.event.shared.EventBus e) {
		this.childEventBus = new ResettableEventBus(eventBus);
		childEventBus.addHandler(ProjectsListEvent.TYPE,
				new ProjectsListEvent.Handler() {

					@Override
					public void onProjectsList(ProjectsListEvent event) {
						populateProjectsList(event.getReadOnlyProjectsList());
					}
				});

		projectsView.setPresenter(this);
		projectsView.setLocked(true);

		receiveData();
		panel.setWidget(projectsView.asWidget());
	}

}
