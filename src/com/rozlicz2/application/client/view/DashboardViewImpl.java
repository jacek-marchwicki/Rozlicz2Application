package com.rozlicz2.application.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.rozlicz2.application.client.tools.MD5Util;

public class DashboardViewImpl extends Composite implements DashboardView {

	interface DashboardViewImplUiBinder extends
			UiBinder<Widget, DashboardViewImpl> {
	}

	private static DashboardViewImplUiBinder uiBinder = GWT
			.create(DashboardViewImplUiBinder.class);

	public static native String getUserEmailFromHtml() /*-{
		return $wnd.userEmail;
	}-*/;

	@UiField
	SimplePanel appWidget;

	@UiField
	SimplePanel breadcrumbPanel;

	@UiField
	Label emailLabel;

	@UiField
	SimplePanel errorPanel;

	@UiField
	Image gravatarImage;

	@UiField
	SimplePanel popupWidget;

	@Inject
	public DashboardViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));

		String email = getUserEmailFromHtml();
		assert email != null;
		emailLabel.setText(email);

		String gravatarImageUrl = getUserGravatarImageUrl(email, 24);
		gravatarImage.setUrl(gravatarImageUrl);

	}

	@Override
	public AcceptsOneWidget getAppWidget() {
		return appWidget;
	}

	@Override
	public AcceptsOneWidget getBreadcrumbPanel() {
		return breadcrumbPanel;
	}

	@Override
	public AcceptsOneWidget getErrorPanel() {
		return errorPanel;
	}

	@Override
	public AcceptsOneWidget getPopupWidget() {
		return popupWidget;
	}

	private String getUserGravatarImageUrl(String email, int size) {

		String hash = MD5Util.md5Hex(email);
		String url = "http://www.gravatar.com/avatar/" + hash + "?s=" + size;

		return url;

	}
}
