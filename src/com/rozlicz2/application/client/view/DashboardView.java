package com.rozlicz2.application.client.view;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

public interface DashboardView extends IsWidget {

	public AcceptsOneWidget getAppWidget();

	public AcceptsOneWidget getErrorPanel();

	public AcceptsOneWidget getPopupWidget();

}