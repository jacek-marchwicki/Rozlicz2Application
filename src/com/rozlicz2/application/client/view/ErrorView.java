package com.rozlicz2.application.client.view;

import java.util.Set;

import com.google.gwt.user.client.ui.IsWidget;

public interface ErrorView extends IsWidget {

	public static class ErrorDisplay {
		private String ErrorInfo;

		public String getErrorInfo() {
			return ErrorInfo;
		}

		public void setErrorInfo(String errorInfo) {
			ErrorInfo = errorInfo;
		}
	}

	void setErrors(Set<ErrorDisplay> errors);

}
