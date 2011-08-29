package com.rozlicz2.application.client.view;

import com.google.gwt.user.client.ui.IsWidget;

public interface ExpenseView extends IsWidget {
	public void setExpenseName(String name);
	public void setPresenter(Presenter presenter);
	public interface Presenter {

		void setExpenseName(String value);
	}
}
