package com.rozlicz2.application.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class DashboardViewMobileImpl extends Composite implements DashboardView {

	interface DashboardViewMobileImplUiBinder extends
			UiBinder<Widget, DashboardViewMobileImpl> {
	}

	private static DashboardViewMobileImplUiBinder uiBinder = GWT
			.create(DashboardViewMobileImplUiBinder.class);

	public static native String getUserEmailFromHtml() /*-{
		return $wnd.userEmail;
	}-*/;

	@UiField
	SimplePanel appWidget;

	@UiField
	Label emailLabel;

	@UiField
	SimplePanel errorPanel;

	@UiField
	SimplePanel popupWidget;

	public DashboardViewMobileImpl() {
		initWidget(uiBinder.createAndBindUi(this));

		String email = getUserEmailFromHtml();
		assert email != null;
		emailLabel.setText(email);
	}

	@Override
	public AcceptsOneWidget getAppWidget() {
		return appWidget;
	}

	@Override
	public AcceptsOneWidget getErrorPanel() {
		return errorPanel;
	}

	@Override
	public AcceptsOneWidget getPopupWidget() {
		return popupWidget;
	}

}
