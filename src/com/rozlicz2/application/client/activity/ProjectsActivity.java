package com.rozlicz2.application.client.activity;

import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.ServerFailure;
import com.rozlicz2.application.client.ClientFactory;
import com.rozlicz2.application.client.DAOManager;
import com.rozlicz2.application.client.dao.SyncDatastoreService;
import com.rozlicz2.application.client.dao.SyncEntity;
import com.rozlicz2.application.client.dao.SyncKey;
import com.rozlicz2.application.client.dao.SyncObserver;
import com.rozlicz2.application.client.dao.SyncPreparedQuery;
import com.rozlicz2.application.client.dao.SyncQuery;
import com.rozlicz2.application.client.place.ProjectPlace;
import com.rozlicz2.application.client.place.ProjectsPlace;
import com.rozlicz2.application.client.view.ProjectsView;
import com.rozlicz2.application.shared.proxy.ProjectListProxy;
import com.rozlicz2.application.shared.service.ListwidgetRequestFactory;
import com.rozlicz2.application.shared.service.ListwidgetRequestFactory.ItemListRequestContext;

public class ProjectsActivity extends AbstractActivity implements
		ProjectsView.Presenter {

	private final ClientFactory clientFactory;
	private final SyncDatastoreService dao;
	private final SyncObserver projectsCountObserver = new SyncObserver() {

		@Override
		public void changed(SyncEntity before, SyncEntity after) {
			updateProjectsCount();
		}
	};;
	private final SyncObserver projectShortObserver = new SyncObserver() {

		@Override
		public void changed(SyncEntity before, SyncEntity after) {
			updateProjectsList();
		}
	};;
	private ProjectsView projectsView;
	private final ListwidgetRequestFactory rf;

	public ProjectsActivity(ProjectsPlace place, ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
		dao = clientFactory.getDAO();
		rf = clientFactory.getRf();
	}

	private void addObservers() {
		dao.addObserver(DAOManager.PROJECTSHORT, projectShortObserver);
		dao.addPropertyObserver(DAOManager.GLOBAL,
				DAOManager.GLOBAL_PROJECTCOUNT, projectsCountObserver);
	}

	@Override
	public void createProject() {
		receiveData();
		/*
		 * SyncEntity syncEntity = new SyncEntity(DAOManager.PROJECT);
		 * syncEntity.setProperty(DAOManager.PROJECT_NAME,
		 * ApplicationConstants.constants.newProject());
		 * syncEntity.setProperty(DAOManager.PROJECT_EXPENSES, new
		 * IdArrayMap<ExpenseEntity>());
		 * syncEntity.setProperty(DAOManager.PROJECT_PARTICIPANTS, new
		 * IdArrayMap<ParticipantEntity>()); dao.put(syncEntity);
		 * 
		 * ProjectPlace place = new ProjectPlace(syncEntity.getKey().getId());
		 * clientFactory.getPlaceController().goTo(place);
		 */
	}

	@Override
	public void editProject(SyncKey key) {

		ProjectPlace place = new ProjectPlace(key.getId());
		clientFactory.getPlaceController().goTo(place);
	}

	@Override
	public void onCancel() {
		removeObservers();
	}

	@Override
	public void onStop() {
		removeObservers();
	}

	private void receiveData() {
		ItemListRequestContext projectListRequest = rf.projectListRequest();
		Request<List<ProjectListProxy>> listAll = projectListRequest.listAll();
		listAll.fire(new Receiver<List<ProjectListProxy>>() {

			@Override
			public void onFailure(ServerFailure error) {
				Window.alert("error: " + error);
				super.onFailure(error);
			}

			@Override
			public void onSuccess(List<ProjectListProxy> response) {
				Window.alert("response: " + response);
			}
		});
	}

	private void removeObservers() {
		dao.removeObserver(DAOManager.PROJECTSHORT, projectShortObserver);
		dao.removePropertyObserver(DAOManager.GLOBAL,
				DAOManager.GLOBAL_PROJECTCOUNT, projectsCountObserver);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		addObservers();

		projectsView = clientFactory.getProjectsView();
		projectsView.setPresenter(this);

		updateProjectsList();
		updateProjectsCount();

		receiveData();
		panel.setWidget(projectsView.asWidget());
	}

	private void updateProjectsCount() {
		SyncQuery q = new SyncQuery(DAOManager.GLOBAL);
		SyncEntity asSingleEntity = dao.prepare(q).asSingleEntity();
		assert (asSingleEntity != null);
		Integer count = (Integer) asSingleEntity
				.getProperty(DAOManager.GLOBAL_PROJECTCOUNT);
		projectsView.setProjectsNumber(count);
	}

	protected void updateProjectsList() {
		SyncQuery q = new SyncQuery(DAOManager.PROJECTSHORT);
		SyncPreparedQuery prepare = dao.prepare(q);

		projectsView.setProjectsList(prepare.asDataProvider());
	}

}
