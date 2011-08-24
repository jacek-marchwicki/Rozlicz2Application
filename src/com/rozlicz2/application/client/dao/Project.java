package com.rozlicz2.application.client.dao;

import java.util.List;

public class Project {
	private long id;
	private String name;
	private List<ExpenseShort> expensesShort;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<ExpenseShort> getExpensesShort() {
		return expensesShort;
	}
	public void setExpensesShort(List<ExpenseShort> expensesShort) {
		this.expensesShort = expensesShort;
	}
}