package com.rozlicz2.application.client.dao;

public class ProjectEntity extends BaseEntity {
	private String name;
	
	public ProjectEntity() {
		super();
	}
	
	public ProjectEntity(String name) {
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
