package com.rozlicz2.application.client.entity;


public class ExpensePaymentEntity extends BaseEntity {
	private String name;
	private double value;

	public String getName() {
		return name;
	}

	public double getValue() {
		return value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(double value) {
		this.value = value;
	}
}