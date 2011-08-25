package com.rozlicz2.application.client.entity;

import java.util.ArrayList;
import java.util.List;

public class ProjectEntity extends ProjectShortEntity {
	private List<ExpenseShortEntity> expensesShort =
		new ArrayList<ExpenseShortEntity>();
	
	public List<ExpenseShortEntity> getExpensesShort() {
		return expensesShort;
	}
	
	public void addExpenseShort(ExpenseShortEntity expense) {
		expensesShort.add(expense);
	}
	
	public void removeExpenseShort(ExpenseShortEntity expense) {
		for (ExpenseShortEntity entity : expensesShort) {
			if (entity.getId() == expense.getId())
			{
				expensesShort.remove(entity);
				return;
			}
		}
	}
}