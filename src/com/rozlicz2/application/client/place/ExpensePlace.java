package com.rozlicz2.application.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class ExpensePlace extends Place {
	
	private final Long expenseId;

	public static class Tokenizer implements PlaceTokenizer<ExpensePlace> {

		@Override
		public ExpensePlace getPlace(String token) {
			return new ExpensePlace(Long.parseLong(token));
		}

		@Override
		public String getToken(ExpensePlace place) {
			return Long.toString(place.getExpenseId());
		}
		
		
		
	}

	public ExpensePlace(Long id) {
		this.expenseId = id;
	}

	public long getExpenseId() {
		return expenseId;
	}

}
