package com.rozlicz2.application.client.view;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.rozlicz2.application.shared.proxy.ExpenseProxy;
import com.rozlicz2.application.shared.proxy.ProjectProxy;

public interface ProjectView extends IsWidget {
	public interface Presenter {
		void createExpense();

		void editExpense(ExpenseProxy expense);

		void save();

		void validate();
	}

	RequestFactoryEditorDriver<ProjectProxy, ?> getDriver();

	void setExpenses(List<ExpenseProxy> expenses);

	void setLocked(boolean locked);

	void setPresenter(Presenter presenter);

}
