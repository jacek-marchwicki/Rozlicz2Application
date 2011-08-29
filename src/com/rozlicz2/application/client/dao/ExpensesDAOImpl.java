package com.rozlicz2.application.client.dao;

import java.util.HashMap;

import com.rozlicz2.application.client.entity.ExpenseEntity;

public class ExpensesDAOImpl implements ExpensesDAO {
	
	private HashMap<Long, ExpenseEntity> all = new HashMap<Long, ExpenseEntity>();

	@Override
	public ExpenseEntity getExpense(long expenseId) {
		return all.get(new Long(expenseId));
	}

	@Override
	public void addExpense(ExpenseEntity entity) {
		all.put(new Long(entity.getId()), entity);
	}

	@Override
	public void removeExpense(ExpenseEntity entity) {
		all.remove(new Long(entity.getId()));
	}

	@Override
	public void save(ExpenseEntity expense) {
		removeExpense(expense);
		addExpense(expense);
	}

}
