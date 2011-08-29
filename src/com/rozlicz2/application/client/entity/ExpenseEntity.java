package com.rozlicz2.application.client.entity;

import com.rozlicz2.application.client.resources.ApplicationConstants;

public class ExpenseEntity extends ExpenseShortEntity {

	public ExpenseEntity(Long projectId) {
		this.setName(ApplicationConstants.constants.emptyExpenditure());
		this.setProjectId(projectId);
	}

}
