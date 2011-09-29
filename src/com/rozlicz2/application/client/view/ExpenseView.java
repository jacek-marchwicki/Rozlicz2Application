package com.rozlicz2.application.client.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.requestfactory.gwt.client.RequestFactoryEditorDriver;
import com.rozlicz2.application.shared.proxy.ExpenseProxy;

public interface ExpenseView extends IsWidget {

	public interface Presenter {
		void addParticipants();

		void save();

		void validate();
	}

	public RequestFactoryEditorDriver<ExpenseProxy, ?> getDriver();

	public void setLocked(boolean locked);

	public void setPresenter(Presenter presenter);

	public void setSum(Double sum);
}
