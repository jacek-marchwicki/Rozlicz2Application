package com.rozlicz2.application.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class BreadcrumbView extends Composite {
	interface BreadcrumbViewUiBinder extends UiBinder<Widget, BreadcrumbView> {
	}

	private static BreadcrumbViewUiBinder uiBinder = GWT
			.create(BreadcrumbViewUiBinder.class);

	@UiField
	Label activityNameLabel;

	public BreadcrumbView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void setName(String name) {
		activityNameLabel.setText(name);
	}
}
