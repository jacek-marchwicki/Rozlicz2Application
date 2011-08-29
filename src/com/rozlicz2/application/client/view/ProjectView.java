package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.rozlicz2.application.client.entity.ExpenseShortEntity;

public interface ProjectView extends IsWidget {
	void setProjectName(String projectName);
	void setExpenses(List<ExpenseShortEntity> expenses);
	public interface Presenter {
		void setProjectName(String projectName);
		void editExpense(Long expenseId);
		void createExpense();
	}
	void setPresenter(Presenter presenter);
	
}
