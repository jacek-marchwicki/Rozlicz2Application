package com.rozlicz2.application.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.rozlicz2.application.client.ClientFactory;
import com.rozlicz2.application.client.DAO;
import com.rozlicz2.application.client.dao.SyncDatastoreService;
import com.rozlicz2.application.client.dao.SyncEntity;
import com.rozlicz2.application.client.dao.SyncKey;
import com.rozlicz2.application.client.dao.SyncObserver;
import com.rozlicz2.application.client.dao.SyncPreparedQuery;
import com.rozlicz2.application.client.dao.SyncQuery;
import com.rozlicz2.application.client.entity.ExpenseShortEntity;
import com.rozlicz2.application.client.entity.IdArrayMap;
import com.rozlicz2.application.client.place.ProjectPlace;
import com.rozlicz2.application.client.place.ProjectsPlace;
import com.rozlicz2.application.client.resources.ApplicationConstants;
import com.rozlicz2.application.client.view.ProjectsView;

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

	public ProjectsActivity(ProjectsPlace place, ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
		dao = clientFactory.getDAO();
	}

	private void addObservers() {
		dao.addObserver(DAO.PROJECTSHORT, projectShortObserver);
		dao.addPropertyObserver(DAO.GLOBAL, DAO.GLOBAL_PROJECTCOUNT,
				projectsCountObserver);
	}

	@Override
	public void createProject() {
		SyncEntity syncEntity = new SyncEntity(DAO.PROJECT);
		syncEntity.setProperty(DAO.PROJECT_NAME,
				ApplicationConstants.constants.newProject());
		syncEntity.setProperty(DAO.PROJECT_EXPENSES,
				new IdArrayMap<ExpenseShortEntity>());
		syncEntity.setProperty(DAO.PROJECT_PARTICIPANTS,
				new IdArrayMap<Participant>());
		dao.put(syncEntity);

		ProjectPlace place = new ProjectPlace(syncEntity.getKey().getId());
		clientFactory.getPlaceController().goTo(place);
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

	private void removeObservers() {
		dao.removeObserver(DAO.PROJECTSHORT, projectShortObserver);
		dao.removePropertyObserver(DAO.GLOBAL, DAO.GLOBAL_PROJECTCOUNT,
				projectsCountObserver);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		projectsView = clientFactory.getProjectsView();
		projectsView.setPresenter(this);

		updateProjectsList();
		updateProjectsCount();
		panel.setWidget(projectsView.asWidget());

		addObservers();
	}

	private void updateProjectsCount() {
		SyncQuery q = new SyncQuery(DAO.GLOBAL);
		SyncEntity asSingleEntity = dao.prepare(q).asSingleEntity();
		assert (asSingleEntity != null);
		Integer count = (Integer) asSingleEntity
				.getProperty(DAO.GLOBAL_PROJECTCOUNT);
		projectsView.setProjectsNumber(count);
	}

	protected void updateProjectsList() {
		SyncQuery q = new SyncQuery(DAO.PROJECTSHORT);
		SyncPreparedQuery prepare = dao.prepare(q);

		projectsView.setProjectsList(prepare.asDataProvider());
	}

}
