package com.rozlicz2.application.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.rozlicz2.application.client.resources.ApplicationResources;

public class BreadcrumbView extends Composite {
	interface BreadcrumbViewUiBinder extends UiBinder<Widget, BreadcrumbView> {
	}

	private static BreadcrumbViewUiBinder uiBinder = GWT
			.create(BreadcrumbViewUiBinder.class);
	String breadcrumbClass = ApplicationResources.INSTANCE.css()
			.breadcrumbClass();

	@UiField
	HTMLPanel breadcrumbPanel;

	String brItemClass = ApplicationResources.INSTANCE.css().brItemClass();

	String homeClass = ApplicationResources.INSTANCE.css().homeClass();

	Label lastLabel;

	public BreadcrumbView() {
		initWidget(uiBinder.createAndBindUi(this));

		breadcrumbPanel.setStyleName(breadcrumbClass);

		Label homeLabel = new Label("Dashboard");
		homeLabel.setStyleName(homeClass);
		breadcrumbPanel.add(homeLabel);

	}

	public void addBreadcrumbItem(String title) {
		// TODO add URL as parameter
		Label lbl = new Label(title);
		lbl.setStylePrimaryName(brItemClass);

		breadcrumbPanel.add(lbl);
		lastLabel = lbl;

	}

	public void changeLastBreadcrumbItemName(String title) {
		if (lastLabel != null)
			lastLabel.setText(title);

	}
}
