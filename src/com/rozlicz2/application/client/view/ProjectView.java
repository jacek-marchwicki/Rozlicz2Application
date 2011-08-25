package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.rozlicz2.application.client.entity.ExpenseShortEntity;

public interface ProjectView extends IsWidget {
	void setProjectName(String projectName);
	void setExpenses(List<ExpenseShortEntity> expenses);
	// TODO setSummary
	public interface Presenter {
		void changeProjectNameTextBox();
	}
	void setPresenter(Presenter presenter);
	
}
