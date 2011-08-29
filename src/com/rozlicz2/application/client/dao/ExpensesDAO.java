package com.rozlicz2.application.client.dao;

import com.rozlicz2.application.client.entity.ExpenseEntity;

public interface ExpensesDAO {
	public ExpenseEntity getExpense(long expenseId);
	public void addExpense(ExpenseEntity entity);
	public void removeExpense(ExpenseEntity entity);
	public void save(ExpenseEntity expense);
}
