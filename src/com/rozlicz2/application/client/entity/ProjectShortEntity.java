package com.rozlicz2.application.client.entity;


public class ProjectShortEntity extends BaseEntity {
	private String name;
	
	public ProjectShortEntity() {
		super();
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
