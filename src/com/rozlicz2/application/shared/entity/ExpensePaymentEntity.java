package com.rozlicz2.application.shared.entity;

import javax.validation.constraints.Size;

public class ExpensePaymentEntity extends BaseEntity {
	@Size(min = 2)
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