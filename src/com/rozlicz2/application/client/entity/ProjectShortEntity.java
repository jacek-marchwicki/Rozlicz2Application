package com.rozlicz2.application.client.entity;

import com.rozlicz2.application.client.resources.ApplicationConstants;


public class ProjectShortEntity extends BaseEntity {
	private String name;
	
	public ProjectShortEntity() {
		super();
		this.name = ApplicationConstants.constants.emptyProject();
	}
	
	public ProjectShortEntity(String name) {
		super();
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
