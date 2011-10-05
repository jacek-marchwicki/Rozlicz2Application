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
import com.rozlicz2.application.shared.entity.Project;
import com.rozlicz2.application.shared.entity.ProjectList;

public class ExpenseDaoImpl extends ObjectifyDao<Expense> implements ExpenseDao {

	static {
		ObjectifyService.register(Expense.class);
	}

	private final ProjectDao projectDao;
	private final ProjectListDao projectListDao;

	@Inject
	public ExpenseDaoImpl(ProjectListDao projectListDao, ProjectDao projectDao) {
		this.projectListDao = projectListDao;
		this.projectDao = projectDao;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.rozlicz2.application.server.service.ExpenseDao#calculateSum(com.rozlicz2
	 * .application.shared.entity.Expense)
	 */
	@Override
	public double calculateSum(Expense expense) {
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
				if (!consumer.isConsumer()) {
					consumer.setValue(0.0);
					continue;
				}
				if (consumer.isProportional()) {
					proportionals++;
					continue;
				}
				toDivide -= consumer.getValue();
			}
			double proportionalValue = toDivide / proportionals;
			for (ExpenseConsumerEntity consumer : consumers) {
				if (!consumer.isConsumer())
					continue;
				if (!consumer.isProportional())
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
		return sum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.rozlicz2.application.server.service.ExpenseDao#findByProjectId(java
	 * .lang.String)
	 */
	@Override
	public List<Expense> findByProjectId(String projectId) {
		return this.listByProperty("projectId", projectId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rozlicz2.application.server.service.ExpenseDao#
	 * refreshParticipantsForProject(java.util.List, java.lang.String)
	 */
	@Override
	public double refreshParticipantsForProject(
			List<ParticipantEntity> participants, String projectId) {
		double sum = 0.0;
		List<Expense> list = this.listByProperty("projectId", projectId);
		for (Expense expense : list) {
			sum += updateExpenseParticipants(expense, participants);
		}
		return sum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.rozlicz2.application.server.service.ExpenseDao#uFind(java.lang.String
	 * )
	 */
	@Override
	public Expense uFind(String id) {
		Expense find = super.find(id);
		if (find == null)
			return null;
		String projectId = find.getProjectId();
		if (projectId == null)
			return null;
		ProjectList findUser = projectListDao.uFindUser(projectId);
		if (findUser == null)
			return null;
		return find;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.rozlicz2.application.server.service.ExpenseDao#uFindByProjectId(java
	 * .lang.String)
	 */
	@Override
	public List<Expense> uFindByProjectId(String projectId) {
		ProjectList projectList = projectListDao.uFindUser(projectId);
		if (projectList == null)
			return null;
		return findByProjectId(projectId);
	}

	private double updateExpenseParticipants(Expense expense,
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
		double sum = calculateSum(expense);
		put(expense);
		return sum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.rozlicz2.application.server.service.ExpenseDao#uSave(com.rozlicz2
	 * .application.shared.entity.Expense)
	 */
	@Override
	public void uSave(Expense list) {
		uSaveAndReturn(list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.rozlicz2.application.server.service.ExpenseDao#uSaveAndReturn(com
	 * .rozlicz2.application.shared.entity.Expense)
	 */
	@Override
	public Expense uSaveAndReturn(Expense expense) {
		String projectId = expense.getProjectId();
		if (projectId == null)
			return null;
		Project expenseProject = projectDao.uFind(projectId);
		if (expenseProject == null)
			return null;
		Expense old = super.find(expense.getId());
		Double oldSum = 0.0;
		if (old != null)
			oldSum = old.getSum();
		List<ParticipantEntity> participants = expenseProject.getParticipants();
		Double newSum = updateExpenseParticipants(expense, participants);
		if (newSum.compareTo(oldSum) != 0) {
			Double projectSum = expenseProject.getSum() - oldSum + newSum;
			expenseProject.setSum(projectSum);
		}
		Key<Expense> key = put(expense);
		try {
			return this.get(key);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
