package com.rozlicz2.application.shared.entity;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Embedded;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Expense extends DatastoreObject {

	@Embedded
	@NotNull
	private List<ExpenseConsumerEntity> consumers = new ArrayList<ExpenseConsumerEntity>();
	@NotNull
	@Size(min = 2)
	private String name;
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

	public void setPayments(List<ExpensePaymentEntity> payments) {
		this.payments = payments;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}
