package com.rozlicz2.application.client.view;

import com.google.gwt.user.client.ui.IsWidget;

public interface ValueTextBoxWidget extends IsWidget {

	public void setPresenter(Presenter presenter);

	public void setValue(double value);

	public static interface Presenter {
		public void setValue(double value);

	}
}
