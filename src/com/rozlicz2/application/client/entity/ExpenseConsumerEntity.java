package com.rozlicz2.application.client.entity;


public class ExpenseConsumerEntity extends BaseEntity {
	private boolean isConsumer;
	private boolean isProportional;
	private String name;
	private Long userId;
	private double value;

	public String getName() {
		return name;
	}

	public Long getUserId() {
		return userId;
	}

	public double getValue() {
		return value;
	}

	public boolean isConsumer() {
		return isConsumer;
	}

	public boolean isProportional() {
		return isProportional;
	}

	public void setConsumer(boolean isConsumer) {
		this.isConsumer = isConsumer;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProportional(boolean isProportional) {
		this.isProportional = isProportional;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setValue(double value) {
		this.value = value;
	}
}