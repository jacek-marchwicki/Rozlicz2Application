package com.rozlicz2.application.shared.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public abstract class BaseEntity {

	@NotNull
	@Size(min = 32, max = 32)
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
