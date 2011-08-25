package com.rozlicz2.application.client.entity;

public class ExpenseShortEntity extends BaseEntity{
	private String name;
	private Long projectName;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setProjectName(Long projectName) {
		this.projectName = projectName;
	}

	public Long getProjectName() {
		return projectName;
	}
}
