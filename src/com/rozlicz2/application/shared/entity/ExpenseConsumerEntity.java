package com.rozlicz2.application.shared.entity;

public class ExpenseConsumerEntity extends BaseEntity {
	private Boolean isConsumer;
	private Boolean isProportional;
	private String name;
	private Double value;

	public String getName() {
		return name;
	}

	public Double getValue() {
		return value;
	}

	public Boolean isConsumer() {
		return isConsumer;
	}

	public Boolean isProportional() {
		return isProportional;
	}

	public void setConsumer(Boolean isConsumer) {
		this.isConsumer = isConsumer;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProportional(Boolean isProportional) {
		this.isProportional = isProportional;
	}

	public void setValue(Double value) {
		this.value = value;
	}
}