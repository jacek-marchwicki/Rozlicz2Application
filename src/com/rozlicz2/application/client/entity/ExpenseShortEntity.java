package com.rozlicz2.application.client.entity;

public class ExpenseShortEntity extends BaseEntity{
	private String name;
	private Long projectId;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Long getProjectId() {
		return projectId;
	}
}
