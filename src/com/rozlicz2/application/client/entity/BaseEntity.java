package com.rozlicz2.application.client.entity;

import java.util.Random;

public class BaseEntity {
	private static Random random;
	private Long id;
	
	public BaseEntity() {
		id = random.nextLong();
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
	
	static {
		random = new Random();
	}
}
