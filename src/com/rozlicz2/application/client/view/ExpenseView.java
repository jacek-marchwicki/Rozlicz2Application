package com.rozlicz2.application.client.view;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;
import com.rozlicz2.application.shared.proxy.ExpenseProxy;

public interface ExpenseView extends IsWidget {

	public interface Presenter {
		void addParticipants();

		void save();
	}

	public SimpleBeanEditorDriver<ExpenseProxy, ?> getDriver();

	public void setPresenter(Presenter presenter);

	public void setSum(Double sum);
}
