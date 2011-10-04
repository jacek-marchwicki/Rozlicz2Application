package com.rozlicz2.application.shared.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.rozlicz2.application.shared.validator.ServerGroup;

public class Expense extends DatastoreObject {
	public static enum PaymentOption {
		EVERYBODY, ONLY_ME, SELECTED_USERS
	}

	@Embedded
	@NotNull
	private List<ExpenseConsumerEntity> consumers = new ArrayList<ExpenseConsumerEntity>();
	@NotNull(groups = { ServerGroup.class })
	private String me;
	@NotNull(groups = { ServerGroup.class })
	private String meId;
	@NotNull
	@Size(min = 4, max = 100)
	private String name;
	@NotNull
	private PaymentOption paymentOption = PaymentOption.EVERYBODY;
	@Embedded
	@NotNull
	private List<ExpensePaymentEntity> payments = new ArrayList<ExpensePaymentEntity>();
	@NotNull
	@Size(min = 32, max = 32)
	private String projectId;
	@NotNull(groups = { ServerGroup.class })
	private Double sum;

	public List<ExpenseConsumerEntity> getConsumers() {
		return consumers;
	}

	public String getMe() {
		return me;
	}

	public String getMeId() {
		return meId;
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

	public Double getSum() {
		return sum;
	}

	public void setConsumers(List<ExpenseConsumerEntity> consumers) {
		this.consumers = consumers;
	}

	public void setMe(String me) {
		this.me = me;
	}

	public void setMeId(String meId) {
		this.meId = meId;
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

	public void setSum(Double sum) {
		this.sum = sum;
	}
}
