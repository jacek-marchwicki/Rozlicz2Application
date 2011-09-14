package com.rozlicz2.application.client.view;

import com.rozlicz2.application.client.entity.BaseEntity;

public class ExpensePayment extends BaseEntity {
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