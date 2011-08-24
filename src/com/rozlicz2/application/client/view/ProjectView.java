package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.rozlicz2.application.client.dao.ExpenseShort;

public interface ProjectView extends IsWidget {
	void setProjectName(String projectName);
	void setExpenses(List<ExpenseShort> expenses);
	// TODO setSummary
	
}
