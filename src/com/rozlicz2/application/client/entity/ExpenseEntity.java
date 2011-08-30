package com.rozlicz2.application.client.entity;

import java.util.Collection;
import java.util.HashMap;

import com.rozlicz2.application.client.resources.ApplicationConstants;

public class ExpenseEntity extends ExpenseShortEntity {
	
	public static class Consumer {
		public Long userId;
		public boolean proportional;
		public Double value;
	}
	public static class Payment {
		public Long userId;
		public Double value;
	}
	
	private HashMap<Long, Payment> payments = new HashMap<Long, ExpenseEntity.Payment>();
	private HashMap<Long, Consumer> consumers = new HashMap<Long, ExpenseEntity.Consumer>();
	
	public ExpenseEntity(Long projectId) {
		this.setName(ApplicationConstants.constants.emptyExpenditure());
		this.setProjectId(projectId);
	}
	
	public Collection<Payment> getPayments() {
		return payments.values();
	}
	
	public Payment getPayment(Long userId) {
		return payments.get(new Long(userId));
	}
	
	public void putPayment(Payment payment) {
		Long key = new Long(payment.userId);
		payments.put(key, payment);
	}
	
	public Collection<Consumer> getConsumers() {
		return consumers.values();
	}
	
	public Consumer getConsumer(Long userId) {
		return consumers.get(new Long(userId));
	}
	
	public void putConsumer(Consumer consumer) {
		Long key = new Long(consumer.userId);
		consumers.put(key, consumer);
	}

}
