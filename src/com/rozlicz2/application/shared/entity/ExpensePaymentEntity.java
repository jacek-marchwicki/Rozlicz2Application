package com.rozlicz2.application.shared.entity;


public class ExpensePaymentEntity extends BaseEntity {
	private String name;
	private double value;

	public String getName() {
		return name;
	}

	public Double getValue() {
		return value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(Double value) {
		this.value = value;
	}
}