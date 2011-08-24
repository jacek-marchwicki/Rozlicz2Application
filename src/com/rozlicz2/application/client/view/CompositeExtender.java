package com.rozlicz2.application.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public abstract class CompositeExtender extends Composite {
	protected void printError(Label errorLabel, String errorMessage, Widget errorSource) {
		errorLabel.setText(errorMessage);
		errorSource.addStyleName("error");
	}
	
	protected void clearError(Label errorLabel, Widget errorSource) {
		errorLabel.setText("");
		errorSource.removeStyleName("error");
	}
	
	protected abstract void validator();
}
