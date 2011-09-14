package com.rozlicz2.application.client.activity;

import com.rozlicz2.application.client.entity.BaseEntity;

public class Participant extends BaseEntity {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}