package com.rozlicz2.application.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.rozlicz2.application.client.entity.ExpenseEntity;
import com.rozlicz2.application.client.entity.IdMap;

public interface ProjectView extends IsWidget {
	public interface Presenter {
		void createExpense();

		void editExpense(long syncKey);

		void setProjectName(String projectName);
	}

	void setExpenses(IdMap<ExpenseEntity> expenses);

	void setPresenter(Presenter presenter);

	void setProjectName(String projectName);

}
