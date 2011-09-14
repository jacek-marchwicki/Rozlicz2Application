package com.rozlicz2.application.client.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.rozlicz2.application.client.ClientFactory;
import com.rozlicz2.application.client.DAO;
import com.rozlicz2.application.client.dao.SyncDatastoreService;
import com.rozlicz2.application.client.dao.SyncEntity;
import com.rozlicz2.application.client.dao.SyncKey;
import com.rozlicz2.application.client.dao.SyncObserver;
import com.rozlicz2.application.client.dao.SyncPreparedQuery;
import com.rozlicz2.application.client.dao.SyncQuery;
import com.rozlicz2.application.client.entity.IdMap;
import com.rozlicz2.application.client.place.ExpensePlace;
import com.rozlicz2.application.client.place.NotFoundPlace;
import com.rozlicz2.application.client.view.AddParticipantView;
import com.rozlicz2.application.client.view.AddParticipantView.Presenter;
import com.rozlicz2.application.client.view.ExpenseConsumer;
import com.rozlicz2.application.client.view.ExpensePayment;
import com.rozlicz2.application.client.view.ExpenseView;

public class ExpenseActivity extends AbstractActivity implements
		ExpenseView.Presenter, Presenter {

	private final ClientFactory clientFactory;
	private final SyncDatastoreService dao;
	private final SyncKey expanseK;
	private final SyncObserver expenseObserver = new SyncObserver() {

		@Override
		public void changed(SyncEntity before, SyncEntity after) {
			refreshView();
		}
	};
	private AddParticipantView participantView;

	private ExpenseView view;

	public ExpenseActivity(ExpensePlace place, ClientFactory clientFactory) {
		this.clientFactory = clientFactory;

		long expenseId = place.getExpenseId();
		expanseK = new SyncKey(DAO.EXPANSE, expenseId);

		dao = clientFactory.getDAO();
	}

	@Override
	public void addedUsers(Collection<String> users) {
		SyncEntity projectE = getProjectEntity();
		@SuppressWarnings("unchecked")
		IdMap<Participant> participants = (IdMap<Participant>) projectE
				.getProperty(DAO.PROJECT_PARTICIPANTS);
		assert (participants != null);
		for (String user : users) {
			SyncQuery q = new SyncQuery(DAO.USER);
			q.addFilter(DAO.USER_NAME, SyncQuery.FilterOperator.EQUAL, user);
			SyncPreparedQuery pq = dao.prepare(q);
			SyncEntity userE = pq.asSingleEntity();
			if (userE == null) {
				userE = new SyncEntity(DAO.USER);
				userE.setProperty(DAO.USER_NAME, user);
				dao.put(userE);
			}
			long userId = userE.getKey().getId();
			String userName = (String) userE.getProperty(DAO.USER_NAME);
			assert (userName != null);
			Participant participant = new Participant();
			participant.setId(userId);
			participant.setName(userName);
			participants.put(participant);
		}
		projectE.setProperty(DAO.PROJECT_PARTICIPANTS, participants);
		dao.put(projectE);
	}

	private void addObservers() {
		dao.addObserver(DAO.EXPANSE, expenseObserver);
	}

	@Override
	public void addParticipants() {
		participantView = clientFactory.getAddParticipantView();
		participantView.setPresenter(this);
		participantView.setUsersList(getUsers());
		RootPanel.get().add(participantView);
		participantView.center();
	}

	@Override
	public void cancel() {
		RootPanel.get().remove(participantView);
	}

	@Override
	public void consumerSet(Long userId, boolean isConsumer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void consumerSetProportional(Long userId, boolean isProportional) {
		// TODO Auto-generated method stub

	}

	@Override
	public void consumerSetValue(Long userId, double value) {
		// TODO Auto-generated method stub

	}

	// private List<ExpenseConsumer> getConsumers(double sum) {
	// ArrayList<ExpenseConsumer> consumers = new
	// ArrayList<ExpenseView.ExpenseConsumer>();
	// List<UserShortEntitiy> perticipants = project.getPerticipants();
	// for (UserShortEntitiy perticipant : perticipants) {
	// ExpenseConsumer consumer = new ExpenseConsumer();
	// consumer.userId = perticipant.getId();
	// consumer.name = perticipant.getName();
	// consumer.isProportional = true;
	// consumer.isConsumer = false;
	//
	// Consumer expenseConsumer = expense.getConsumer(perticipant.getId());
	// if (expenseConsumer != null) {
	// consumer.isProportional = expenseConsumer.proportional;
	// consumer.isConsumer = true;
	// consumer.value = expenseConsumer.value;
	// }
	// consumers.add(consumer);
	// }
	// int proportionals = 0;
	// for (ExpenseConsumer consumer : consumers) {
	// if (!consumer.isConsumer)
	// continue;
	// if (consumer.isProportional) {
	// proportionals++;
	// continue;
	// }
	// sum -= consumer.value;
	// }
	// double valuePerProportional = sum / (double) proportionals;
	// for (ExpenseConsumer consumer : consumers) {
	// if (!consumer.isConsumer)
	// continue;
	// if (!consumer.isProportional)
	// continue;
	// consumer.value = valuePerProportional;
	// }
	// return consumers;
	// }

	// private List<ExpensePayment> getPayments() {
	// ArrayList<ExpensePayment> payments = new
	// ArrayList<ExpenseView.ExpensePayment>();
	// List<UserShortEntitiy> perticipants = project.getPerticipants();
	// for (UserShortEntitiy perticipant : perticipants) {
	// ExpensePayment payment = new ExpensePayment();
	// payment.userId = perticipant.getId();
	// payment.name = perticipant.getName();
	// payment.value = 0.0;
	// Payment expensePayment = expense.getPayment(perticipant.getId());
	// if (expensePayment != null) {
	// payment.value = expensePayment.value;
	// }
	// payments.add(payment);
	// }
	// return payments;
	// }

	private double getPaymentsSum(IdMap<ExpensePayment> payments) {
		double ret = 0.0;
		for (ExpensePayment payment : payments.values()) {
			ret += payment.value;
		}
		return ret;
	}

	private SyncEntity getProjectEntity() {
		SyncEntity expanseE = dao.get(expanseK);
		assert (expanseE != null);
		Long projectId = (Long) expanseE.getProperty(DAO.EXPANSE_PROJECTID);
		assert (projectId != null);
		SyncKey projectK = new SyncKey(DAO.PROJECT, projectId);
		return dao.get(projectK);
	}

	private List<String> getUsers() {
		SyncEntity projectE = getProjectEntity();
		@SuppressWarnings("unchecked")
		IdMap<Participant> participants = (IdMap<Participant>) projectE
				.getProperty(DAO.PROJECT_PARTICIPANTS);

		ArrayList<String> users = new ArrayList<String>();
		SyncQuery q = new SyncQuery(DAO.USER);
		SyncPreparedQuery pq = dao.prepare(q);
		for (SyncEntity user : pq.asIterable()) {
			long userId = user.getKey().getId();
			if (participants.containsKey(userId))
				continue;
			String userName = (String) user.getProperty(DAO.USER_NAME);
			assert (userName != null);
			users.add(userName);
		}
		return users;
	}

	private void notFoundPlace() {
		Place place = new NotFoundPlace();
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

	@Override
	public void paymentSetValue(Long userId, double value) {
		// TODO Auto-generated method stub

	}

	private void refreshView() {
		SyncEntity syncEntity = dao.get(expanseK);
		if (syncEntity == null) {
			notFoundPlace();
			return;
		}
		@SuppressWarnings("unchecked")
		IdMap<ExpensePayment> payments = (IdMap<ExpensePayment>) syncEntity
				.getProperty(DAO.EXPANSE_PAYMENTS);
		assert (payments != null);
		@SuppressWarnings("unchecked")
		IdMap<ExpenseConsumer> consumers = (IdMap<ExpenseConsumer>) syncEntity
				.getProperty(DAO.EXPANSE_CONSUMERS);
		assert (consumers != null);

		Double sum = (Double) syncEntity.getProperty(DAO.EXPANSE_SUM);
		assert (sum != null);

		String name = (String) syncEntity.getProperty(DAO.EXPANSE_NAME);
		assert (name != null);

		view.setExpenseName(name);
		view.setConsumers(consumers);
		view.setPayments(payments);
		view.setPaymentsSum(sum);
	}

	private void removeObservers() {
		dao.removeObserver(DAO.EXPANSE, expenseObserver);
	}

	@Override
	public void setExpenseName(String value) {
		SyncEntity syncEntity = dao.get(expanseK);
		syncEntity.setProperty(DAO.EXPANSE_NAME, value);
		dao.put(syncEntity);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view = clientFactory.getExpenseView();
		view.setPresenter(this);

		refreshView();

		panel.setWidget(view.asWidget());

		addObservers();
	}

}
