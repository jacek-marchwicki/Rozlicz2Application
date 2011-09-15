package com.rozlicz2.application.client.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.rozlicz2.application.client.ClientFactory;
import com.rozlicz2.application.client.DAOManager;
import com.rozlicz2.application.client.dao.SyncDatastoreService;
import com.rozlicz2.application.client.dao.SyncEntity;
import com.rozlicz2.application.client.dao.SyncKey;
import com.rozlicz2.application.client.dao.SyncObserver;
import com.rozlicz2.application.client.dao.SyncPreparedQuery;
import com.rozlicz2.application.client.dao.SyncQuery;
import com.rozlicz2.application.client.entity.ExpenseConsumerEntity;
import com.rozlicz2.application.client.entity.ExpenseEntity;
import com.rozlicz2.application.client.entity.ExpensePaymentEntity;
import com.rozlicz2.application.client.entity.IdMap;
import com.rozlicz2.application.client.entity.ParticipantEntity;
import com.rozlicz2.application.client.place.ExpensePlace;
import com.rozlicz2.application.client.place.NotFoundPlace;
import com.rozlicz2.application.client.view.AddParticipantView;
import com.rozlicz2.application.client.view.AddParticipantView.Presenter;
import com.rozlicz2.application.client.view.ExpenseView;

public class ExpenseActivity extends AbstractActivity implements
		ExpenseView.Presenter, Presenter {

	private final ClientFactory clientFactory;
	private final SyncDatastoreService dao;
	private final SyncKey expenseK;
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
		expenseK = new SyncKey(DAOManager.EXPANSE, expenseId);

		dao = clientFactory.getDAO();
	}

	@Override
	public void addedUsers(Collection<String> users) {
		addUsersToProject(users);

		updateProjectEntitiesParticipants();

		RootPanel.get().remove(participantView);
	}

	private void addObservers() {
		dao.addObserver(DAOManager.EXPANSE, expenseObserver);
	}

	@Override
	public void addParticipants() {
		participantView = clientFactory.getAddParticipantView();
		participantView.setPresenter(this);
		participantView.setUsersList(getUsers());
		RootPanel.get().add(participantView);
		participantView.center();
	}

	private void addUsersToProject(Collection<String> users) {
		SyncEntity projectE = getProjectEntity();
		@SuppressWarnings("unchecked")
		IdMap<ParticipantEntity> participants = (IdMap<ParticipantEntity>) projectE
				.getProperty(DAOManager.PROJECT_PARTICIPANTS);
		assert (participants != null);
		for (String user : users) {
			SyncQuery q = new SyncQuery(DAOManager.USER);
			q.addFilter(DAOManager.USER_NAME, SyncQuery.FilterOperator.EQUAL,
					user);
			SyncPreparedQuery pq = dao.prepare(q);
			SyncEntity userE = pq.asSingleEntity();
			if (userE == null) {
				userE = new SyncEntity(DAOManager.USER);
				userE.setProperty(DAOManager.USER_NAME, user);
				dao.put(userE);

			}
			long userId = userE.getKey().getId();
			String userName = (String) userE.getProperty(DAOManager.USER_NAME);
			assert (userName != null);
			ParticipantEntity participant = new ParticipantEntity();
			participant.setId(userId);
			participant.setName(userName);
			participants.put(participant);
		}
		projectE.setProperty(DAOManager.PROJECT_PARTICIPANTS, participants);
		dao.put(projectE);
	}

	@Override
	public void cancel() {
		RootPanel.get().remove(participantView);
	}

	@Override
	public void consumerSet(Long userId, boolean isConsumer) {
		SyncEntity expenseE = dao.get(expenseK);
		@SuppressWarnings("unchecked")
		IdMap<ExpenseConsumerEntity> consumers = (IdMap<ExpenseConsumerEntity>) expenseE
				.getProperty(DAOManager.EXPANSE_CONSUMERS);
		ExpenseConsumerEntity expenseConsumerEntity = consumers.get(userId);
		expenseConsumerEntity.setConsumer(isConsumer);
		expenseE.setProperty(DAOManager.EXPANSE_CONSUMERS, consumers);
		dao.put(expenseE);
		updateThisEntityValues();
	}

	@Override
	public void consumerSetProportional(Long userId, boolean isProportional) {
		SyncEntity expenseE = dao.get(expenseK);
		@SuppressWarnings("unchecked")
		IdMap<ExpenseConsumerEntity> consumers = (IdMap<ExpenseConsumerEntity>) expenseE
				.getProperty(DAOManager.EXPANSE_CONSUMERS);
		ExpenseConsumerEntity expenseConsumerEntity = consumers.get(userId);
		expenseConsumerEntity.setProportional(isProportional);
		expenseE.setProperty(DAOManager.EXPANSE_CONSUMERS, consumers);
		dao.put(expenseE);
		updateThisEntityValues();
	}

	@Override
	public void consumerSetValue(Long userId, double value) {
		SyncEntity expenseE = dao.get(expenseK);
		@SuppressWarnings("unchecked")
		IdMap<ExpenseConsumerEntity> consumers = (IdMap<ExpenseConsumerEntity>) expenseE
				.getProperty(DAOManager.EXPANSE_CONSUMERS);
		ExpenseConsumerEntity expenseConsumerEntity = consumers.get(userId);
		expenseConsumerEntity.setValue(value);
		expenseE.setProperty(DAOManager.EXPANSE_CONSUMERS, consumers);
		dao.put(expenseE);
		updateThisEntityValues();
	}

	private SyncEntity getProjectEntity() {
		SyncEntity expanseE = dao.get(expenseK);
		assert (expanseE != null);
		Long projectId = (Long) expanseE
				.getProperty(DAOManager.EXPANSE_PROJECTID);
		assert (projectId != null);
		SyncKey projectK = new SyncKey(DAOManager.PROJECT, projectId);
		return dao.get(projectK);
	}

	private List<String> getUsers() {
		SyncEntity projectE = getProjectEntity();
		@SuppressWarnings("unchecked")
		IdMap<ParticipantEntity> participants = (IdMap<ParticipantEntity>) projectE
				.getProperty(DAOManager.PROJECT_PARTICIPANTS);

		ArrayList<String> users = new ArrayList<String>();
		SyncQuery q = new SyncQuery(DAOManager.USER);
		SyncPreparedQuery pq = dao.prepare(q);
		for (SyncEntity user : pq.asIterable()) {
			long userId = user.getKey().getId();
			if (participants.containsKey(userId))
				continue;
			String userName = (String) user.getProperty(DAOManager.USER_NAME);
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
		SyncEntity expenseE = dao.get(expenseK);
		@SuppressWarnings("unchecked")
		IdMap<ExpensePaymentEntity> payments = (IdMap<ExpensePaymentEntity>) expenseE
				.getProperty(DAOManager.EXPANSE_PAYMENTS);
		ExpensePaymentEntity payment = payments.get(userId);
		payment.setValue(value);
		expenseE.setProperty(DAOManager.EXPANSE_PAYMENTS, payments);
		dao.put(expenseE);
		updateThisEntityValues();
	}

	private void refreshView() {
		SyncEntity syncEntity = dao.get(expenseK);
		if (syncEntity == null) {
			notFoundPlace();
			return;
		}
		@SuppressWarnings("unchecked")
		IdMap<ExpensePaymentEntity> payments = (IdMap<ExpensePaymentEntity>) syncEntity
				.getProperty(DAOManager.EXPANSE_PAYMENTS);
		assert (payments != null);
		@SuppressWarnings("unchecked")
		IdMap<ExpenseConsumerEntity> consumers = (IdMap<ExpenseConsumerEntity>) syncEntity
				.getProperty(DAOManager.EXPANSE_CONSUMERS);
		assert (consumers != null);

		Double sum = (Double) syncEntity.getProperty(DAOManager.EXPANSE_SUM);
		assert (sum != null);

		String name = (String) syncEntity.getProperty(DAOManager.EXPANSE_NAME);
		assert (name != null);

		view.setExpenseName(name);
		view.setConsumers(consumers);
		view.setPayments(payments);
		view.setPaymentsSum(sum);
	}

	private void removeObservers() {
		dao.removeObserver(DAOManager.EXPANSE, expenseObserver);
	}

	@Override
	public void setExpenseName(String value) {
		SyncEntity syncEntity = dao.get(expenseK);
		syncEntity.setProperty(DAOManager.EXPANSE_NAME, value);
		dao.put(syncEntity);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		addObservers();

		view = clientFactory.getExpenseView();
		view.setPresenter(this);

		refreshView();

		panel.setWidget(view.asWidget());
	}

	protected void updateConsumers(final IdMap<ParticipantEntity> participants,
			final IdMap<ExpenseConsumerEntity> consumers) {
		for (ParticipantEntity participant : participants.values()) {
			ExpenseConsumerEntity consumer = consumers.get(participant.getId());
			if (consumer == null) {
				consumer = new ExpenseConsumerEntity();
				consumer.setProportional(true);
				consumer.setConsumer(false);
				consumer.setId(participant.getId());
				consumer.setValue(0);
				consumers.put(consumer);
			}
			consumer.setName(participant.getName());
			consumers.put(consumer);
		}
		for (Iterator<ExpenseConsumerEntity> i = consumers.values().iterator(); i
				.hasNext();) {
			ExpenseConsumerEntity consumer = i.next();
			long consumerId = consumer.getId();
			if (!participants.containsKey(consumerId))
				i.remove();
		}

	}

	private double updateEntityValues(IdMap<ExpensePaymentEntity> payments,
			IdMap<ExpenseConsumerEntity> consumers) {
		double sum = 0;
		for (ExpensePaymentEntity payment : payments.values()) {
			sum += payment.getValue();
		}
		int proportionals = 0;
		double value = sum;
		for (ExpenseConsumerEntity consumer : consumers.values()) {
			if (!consumer.isConsumer())
				continue;
			if (consumer.isProportional()) {
				proportionals++;
				continue;
			}
			value -= consumer.getValue();
		}
		double proportionalValue = value / proportionals;
		for (ExpenseConsumerEntity consumer : consumers.values()) {
			if (!consumer.isConsumer())
				continue;
			if (!consumer.isProportional())
				continue;
			consumer.setValue(proportionalValue);
		}
		return sum;
	}

	protected void updatePayments(IdMap<ParticipantEntity> participants,
			IdMap<ExpensePaymentEntity> payments) {
		for (ParticipantEntity participant : participants.values()) {
			ExpensePaymentEntity payment = payments.get(participant.getId());
			if (payment == null) {
				payment = new ExpensePaymentEntity();
				payment.setId(participant.getId());
				payment.setValue(0);
			}
			payment.setName(participant.getName());
			payments.put(payment);
		}
		for (Iterator<ExpensePaymentEntity> i = payments.values().iterator(); i
				.hasNext();) {
			ExpensePaymentEntity payment = i.next();
			long consumerId = payment.getId();
			if (!participants.containsKey(consumerId))
				i.remove();
		}
	}

	private void updateProjectEntitiesParticipants() {
		SyncEntity projectEntity = getProjectEntity();
		@SuppressWarnings("unchecked")
		IdMap<ParticipantEntity> participants = (IdMap<ParticipantEntity>) projectEntity
				.getProperty(DAOManager.PROJECT_PARTICIPANTS);
		@SuppressWarnings("unchecked")
		IdMap<ExpenseEntity> expenses = (IdMap<ExpenseEntity>) projectEntity
				.getProperty(DAOManager.PROJECT_EXPENSES);
		assert (expenses != null);
		for (Long expenseId : expenses.keySet()) {
			SyncKey expenseK = new SyncKey(DAOManager.EXPANSE, expenseId);
			SyncEntity expenseE = dao.get(expenseK);
			assert (expenseE != null);
			@SuppressWarnings("unchecked")
			IdMap<ExpenseConsumerEntity> consumers = (IdMap<ExpenseConsumerEntity>) expenseE
					.getProperty(DAOManager.EXPANSE_CONSUMERS);
			assert (consumers != null);
			@SuppressWarnings("unchecked")
			IdMap<ExpensePaymentEntity> payments = (IdMap<ExpensePaymentEntity>) expenseE
					.getProperty(DAOManager.EXPANSE_PAYMENTS);
			assert (payments != null);

			updateConsumers(participants, consumers);
			updatePayments(participants, payments);
			double sum = updateEntityValues(payments, consumers);

			expenseE.setProperty(DAOManager.EXPANSE_CONSUMERS, consumers);
			expenseE.setProperty(DAOManager.EXPANSE_PAYMENTS, payments);
			expenseE.setProperty(DAOManager.EXPANSE_SUM, new Double(sum));

			dao.put(expenseE);
		}
	}

	private void updateThisEntityValues() {
		SyncEntity expenseE = dao.get(expenseK);

		@SuppressWarnings("unchecked")
		IdMap<ExpensePaymentEntity> payments = (IdMap<ExpensePaymentEntity>) expenseE
				.getProperty(DAOManager.EXPANSE_PAYMENTS);
		assert (payments != null);

		@SuppressWarnings("unchecked")
		IdMap<ExpenseConsumerEntity> consumers = (IdMap<ExpenseConsumerEntity>) expenseE
				.getProperty(DAOManager.EXPANSE_CONSUMERS);
		assert (consumers != null);

		double sum = updateEntityValues(payments, consumers);

		expenseE.setProperty(DAOManager.EXPANSE_CONSUMERS, consumers);
		expenseE.setProperty(DAOManager.EXPANSE_PAYMENTS, payments);
		expenseE.setProperty(DAOManager.EXPANSE_SUM, new Double(sum));
		dao.put(expenseE);
	}

}
