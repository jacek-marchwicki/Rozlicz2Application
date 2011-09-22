package com.rozlicz2.application.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;
import com.rozlicz2.application.shared.proxy.ExpenseProxy;

public class ExpensePlace extends Place {

	@Prefix("expense")
	public static class Tokenizer implements PlaceTokenizer<ExpensePlace> {

		@Override
		public ExpensePlace getPlace(String token) {
			return new ExpensePlace(token);
		}

		@Override
		public String getToken(ExpensePlace place) {
			return place.getExpenseId();
		}

	}

	private ExpenseProxy expense;
	private String expenseId;

	public ExpensePlace(ExpenseProxy expense) {
		this.expense = expense;
		this.expenseId = expense.getId();
	}

	public ExpensePlace(String expenseId) {
		this.expenseId = expenseId;
	}

	public ExpenseProxy getExpense() {
		return expense;
	}

	public String getExpenseId() {
		return expenseId;
	}

	public void setExpense(ExpenseProxy expense) {
		this.expense = expense;
	}

	public void setExpenseId(String expenseId) {
		this.expenseId = expenseId;
	}

}
