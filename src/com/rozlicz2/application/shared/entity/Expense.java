package com.rozlicz2.application.shared.entity;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Embedded;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Expense extends DatastoreObject {
	public static enum PaymentOption {
		EVERYBODY, ONLY_ME, SELECTED_USERS
	}

	@Embedded
	@NotNull
	private List<ExpenseConsumerEntity> consumers = new ArrayList<ExpenseConsumerEntity>();
	@NotNull
	@Size(min = 2)
	private String name;
	@NotNull
	private PaymentOption paymentOption = PaymentOption.EVERYBODY;
	@Embedded
	@NotNull
	private List<ExpensePaymentEntity> payments = new ArrayList<ExpensePaymentEntity>();
	@NotNull
	@Size(min = 32, max = 32)
	private String projectId;

	public List<ExpenseConsumerEntity> getConsumers() {
		return consumers;
	}

	public String getName() {
		return name;
	}

	public PaymentOption getPaymentOption() {
		return paymentOption;
	}

	public List<ExpensePaymentEntity> getPayments() {
		return payments;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setConsumers(List<ExpenseConsumerEntity> consumers) {
		this.consumers = consumers;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPaymentOption(PaymentOption paymentOption) {
		this.paymentOption = paymentOption;
	}

	public void setPayments(List<ExpensePaymentEntity> payments) {
		this.payments = payments;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}
