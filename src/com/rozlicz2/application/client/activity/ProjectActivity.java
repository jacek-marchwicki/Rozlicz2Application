package com.rozlicz2.application.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.rozlicz2.application.client.ClientFactory;
import com.rozlicz2.application.client.DAO;
import com.rozlicz2.application.client.dao.SyncDatastoreService;
import com.rozlicz2.application.client.dao.SyncEntity;
import com.rozlicz2.application.client.dao.SyncKey;
import com.rozlicz2.application.client.dao.SyncObserver;
import com.rozlicz2.application.client.entity.ExpenseConsumerEntity;
import com.rozlicz2.application.client.entity.ExpenseEntity;
import com.rozlicz2.application.client.entity.ExpensePaymentEntity;
import com.rozlicz2.application.client.entity.IdArrayMap;
import com.rozlicz2.application.client.entity.IdMap;
import com.rozlicz2.application.client.place.ExpensePlace;
import com.rozlicz2.application.client.place.NotFoundPlace;
import com.rozlicz2.application.client.place.ProjectPlace;
import com.rozlicz2.application.client.resources.ApplicationConstants;
import com.rozlicz2.application.client.view.ProjectView;

public class ProjectActivity extends AbstractActivity implements
		ProjectView.Presenter {

	private final ClientFactory clientFactory;
	private final SyncDatastoreService dao;
	private final SyncKey projectKey;
	private final SyncObserver projectObserver = new SyncObserver() {
		@Override
		public void changed(SyncEntity before, SyncEntity after) {
			projectChanged();
		}
	};
	private ProjectView projectView;

	public ProjectActivity(ProjectPlace place, ClientFactory clientFactory) {
		long id = place.getProjectId();
		projectKey = new SyncKey(DAO.PROJECT, id);
		this.clientFactory = clientFactory;
		dao = clientFactory.getDAO();
	}

	private void addObservers() {
		dao.addObserver(DAO.PROJECT, projectObserver);
	}

	@Override
	public void createExpense() {
		SyncEntity expanseE = new SyncEntity(DAO.EXPANSE);
		expanseE.setProperty(DAO.EXPANSE_NAME,
				ApplicationConstants.constants.newExpense());
		expanseE.setProperty(DAO.EXPANSE_PROJECTID, projectKey.getId());
		expanseE.setProperty(DAO.EXPANSE_PAYMENTS,
				new IdArrayMap<ExpensePaymentEntity>());
		expanseE.setProperty(DAO.EXPANSE_CONSUMERS,
				new IdArrayMap<ExpenseConsumerEntity>());
		expanseE.setProperty(DAO.EXPANSE_SUM, new Double(0));
		dao.put(expanseE);

		ExpensePlace place = new ExpensePlace(expanseE.getKey().getId());
		clientFactory.getPlaceController().goTo(place);
	}

	@Override
	public void editExpense(long expenseId) {
		ExpensePlace place = new ExpensePlace(expenseId);
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

	protected void projectChanged() {
		SyncEntity project = dao.get(projectKey);
		if (project == null) {
			Place place = new NotFoundPlace();
			clientFactory.getPlaceController().goTo(place);
			return;
		}
		@SuppressWarnings("unchecked")
		IdMap<ExpenseEntity> expenses = (IdMap<ExpenseEntity>) project
				.getProperty(DAO.PROJECT_EXPENSES);
		assert (expenses != null);
		String name = (String) project.getProperty(DAO.PROJECT_NAME);
		projectView.setExpenses(expenses);
		projectView.setProjectName(name);
	}

	private void removeObservers() {
		dao.removeObserver(DAO.PROJECT, projectObserver);
	}

	@Override
	public void setProjectName(String projectName) {
		SyncEntity project = dao.get(projectKey);
		project.setProperty(DAO.PROJECT_NAME, projectName);
		dao.put(project);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		addObservers();

		projectView = clientFactory.getProjectView();
		projectView.setPresenter(this);

		projectChanged();

		panel.setWidget(projectView.asWidget());
	}

}
