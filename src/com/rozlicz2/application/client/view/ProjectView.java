package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;
import com.rozlicz2.application.shared.proxy.ExpenseProxy;
import com.rozlicz2.application.shared.proxy.ProjectProxy;

public interface ProjectView extends IsWidget {
	public interface Presenter {
		void createExpense();

		void editExpense(ExpenseProxy expense);

		void save();
	}

	SimpleBeanEditorDriver<ProjectProxy, ?> getDriver();

	void setExpenses(List<ExpenseProxy> expenses);

	void setPresenter(Presenter presenter);

}
