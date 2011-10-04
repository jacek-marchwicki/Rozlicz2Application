package com.rozlicz2.application.shared.entity;


public class ExpenseConsumerEntity extends BaseEntity {
	private Boolean consumer;
	private Boolean proportional;
	private String name;
	private Double value;

	public String getName() {
		return name;
	}

	public Double getValue() {
		return value;
	}

	public Boolean isConsumer() {
		return consumer;
	}

	public Boolean isProportional() {
		return proportional;
	}

	public void setConsumer(Boolean isConsumer) {
		this.consumer = isConsumer;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setProportional(Boolean isProportional) {
		this.proportional = isProportional;
	}

	public void setValue(Double value) {
		this.value = value;
	}
}