package com.rozlicz2.application.server.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.rozlicz2.application.shared.entity.Expense;
import com.rozlicz2.application.shared.entity.Expense.PaymentOption;
import com.rozlicz2.application.shared.entity.ExpenseConsumerEntity;
import com.rozlicz2.application.shared.entity.ExpensePaymentEntity;
import com.rozlicz2.application.shared.entity.ParticipantEntity;
import com.rozlicz2.application.shared.entity.ProjectList;

public class ExpenseDao extends ObjectifyDao<Expense> {

	static {
		ObjectifyService.register(Expense.class);
	}

	private final ProjectListDao projectListDao;

	@Inject
	public ExpenseDao(ProjectListDao projectListDao) {
		this.projectListDao = projectListDao;
	}

	public void calculateSum(Expense expense) {
		List<ExpensePaymentEntity> payments = expense.getPayments();
		List<ExpenseConsumerEntity> consumers = expense.getConsumers();
		PaymentOption paymentOption = expense.getPaymentOption();
		String meId = expense.getMeId();
		assert payments != null;
		assert consumers != null;
		assert paymentOption != null;
		assert meId != null;
		double sum = 0.0;
		for (ExpensePaymentEntity payment : payments) {
			sum += payment.getValue();
		}
		expense.setSum(sum);
		if (paymentOption.equals(PaymentOption.SELECTED_USERS)) {
			double toDivide = sum;
			int proportionals = 0;
			for (ExpenseConsumerEntity consumer : consumers) {
				if (!consumer.getIsConsumer())
					continue;
				if (consumer.getIsProportional()) {
					proportionals++;
					continue;
				}
				toDivide -= consumer.getValue();
			}
			double proportionalValue = toDivide / proportionals;
			for (ExpenseConsumerEntity consumer : consumers) {
				if (!consumer.getIsConsumer())
					continue;
				if (!consumer.getIsProportional())
					continue;
				consumer.setValue(proportionalValue);
			}
			expense.setConsumers(consumers);
		} else if (paymentOption.equals(PaymentOption.EVERYBODY)) {
			Double proportionalValue = sum / consumers.size();
			for (ExpenseConsumerEntity consumer : consumers) {
				consumer.setConsumer(true);
				consumer.setValue(proportionalValue);
				consumer.setProportional(true);
			}
			expense.setConsumers(consumers);
		} else if (paymentOption.equals(PaymentOption.ONLY_ME)) {
			for (ExpenseConsumerEntity consumer : consumers) {
				if (consumer.getId().equals(meId)) {
					consumer.setValue(sum);
					consumer.setProportional(true);
					consumer.setConsumer(true);
				} else {
					consumer.setValue(0.0);
					consumer.setProportional(true);
					consumer.setConsumer(false);
				}
			}
			expense.setConsumers(consumers);
		}
	}

	@Override
	public Expense find(String id) {
		Expense find = super.find(id);
		if (find == null)
			return null;
		String projectId = find.getProjectId();
		if (projectId == null)
			return null;
		ProjectList findUser = projectListDao.findUser(projectId);
		if (findUser == null)
			return null;
		return find;
	}

	public List<Expense> findByProjectId(String projectId) {
		ProjectList projectList = projectListDao.findUser(projectId);
		if (projectList == null)
			return null;
		List<Expense> list = this.listByProperty("projectId", projectId);
		return list;
	}

	public void refreshParticipantsForProject(
			List<ParticipantEntity> participants, String projectId) {
		List<Expense> list = this.listByProperty("projectId", projectId);
		for (Expense expense : list) {
			updateExpenseParticipants(expense, participants);
		}
	}

	public void save(Expense list) {
		saveAndReturn(list);
	}

	public Expense saveAndReturn(Expense list) {
		String projectId = list.getProjectId();
		if (projectId == null)
			return null;
		ProjectList findUser = projectListDao.findUser(projectId);
		if (findUser == null)
			return null;
		calculateSum(list);
		Key<Expense> key = put(list);
		try {
			return this.get(key);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void updateExpenseParticipants(Expense expense,
			List<ParticipantEntity> participants) {
		Map<String, ParticipantEntity> participantsMap = new HashMap<String, ParticipantEntity>();
		for (ParticipantEntity participant : participants) {
			participantsMap.put(participant.getId(), participant);
		}
		Map<String, Boolean> ids = new HashMap<String, Boolean>();
		List<ExpenseConsumerEntity> oldConsumers = expense.getConsumers();
		List<ExpenseConsumerEntity> newConsumers = new ArrayList<ExpenseConsumerEntity>();
		for (ExpenseConsumerEntity oldConsumer : oldConsumers) {
			ParticipantEntity matchingParticipant = participantsMap
					.get(oldConsumer.getId());
			if (matchingParticipant == null)
				continue;
			oldConsumer.setName(matchingParticipant.getName());
			newConsumers.add(oldConsumer);
			ids.put(oldConsumer.getId(), new Boolean(true));
		}
		for (ParticipantEntity participant : participants) {
			Boolean isInBase = ids.get(participant.getId());
			if (isInBase != null)
				continue;
			ExpenseConsumerEntity consumer = new ExpenseConsumerEntity();
			consumer.setId(participant.getId());
			consumer.setName(participant.getName());
			consumer.setProportional(true);
			consumer.setConsumer(false);
			consumer.setValue(0.0);
			newConsumers.add(consumer);
		}
		expense.setConsumers(newConsumers);

		ids = new HashMap<String, Boolean>();
		List<ExpensePaymentEntity> oldPayments = expense.getPayments();
		List<ExpensePaymentEntity> newPayments = new ArrayList<ExpensePaymentEntity>();
		for (ExpensePaymentEntity oldPayment : oldPayments) {
			ParticipantEntity matchingParticipant = participantsMap
					.get(oldPayment.getId());
			if (matchingParticipant == null)
				continue;
			oldPayment.setName(matchingParticipant.getName());
			newPayments.add(oldPayment);
			ids.put(oldPayment.getId(), new Boolean(true));
		}
		for (ParticipantEntity participant : participants) {
			Boolean isInBase = ids.get(participant.getId());
			if (isInBase != null)
				continue;
			ExpensePaymentEntity payment = new ExpensePaymentEntity();
			payment.setId(participant.getId());
			payment.setName(participant.getName());
			payment.setValue(0.0);
			newPayments.add(payment);
		}

		expense.setPayments(newPayments);
		calculateSum(expense);
		put(expense);
	}

}
