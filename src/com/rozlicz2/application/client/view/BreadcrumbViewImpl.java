package com.rozlicz2.application.client.view;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.rozlicz2.application.client.resources.ApplicationResources;

public class BreadcrumbViewImpl extends Composite implements BreadcrumbView {
	interface BreadcrumbViewImplUiBinder extends
			UiBinder<Widget, BreadcrumbViewImpl> {
	}

	private static BreadcrumbViewImplUiBinder uiBinder = GWT
			.create(BreadcrumbViewImplUiBinder.class);
	String breadcrumbClass = ApplicationResources.INSTANCE.css()
			.breadcrumbClass();

	@UiField
	HTMLPanel breadcrumbPanel;

	String brItemClass = ApplicationResources.INSTANCE.css().brItemClass();

	String homeClass = ApplicationResources.INSTANCE.css().homeClass();

	private final Map<String, Label> itemKeyMap = new HashMap<String, Label>();

	Label lastLabel;
	private Presenter presenter;

	public BreadcrumbViewImpl() {
		initWidget(uiBinder.createAndBindUi(this));

		breadcrumbPanel.setStyleName(breadcrumbClass);

		Label homeLabel = new Label("Dashboard");
		homeLabel.setStyleName(homeClass);
		breadcrumbPanel.add(homeLabel);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.rozlicz2.application.client.view.BreadcrumbView#addBreadcrumbItem
	 * (java.lang.String, java.lang.String, java.lang.String)
	 */

	@Override
	public void addBreadcrumbItem(final String key, String title) {
		// TODO remove URL as parameter

		Label lbl = itemKeyMap.get(key);
		if (lbl == null) {
			lbl = new Label();
			lbl.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					presenter.gotoKey(key);
				}
			});
			lbl.setStylePrimaryName(brItemClass);
			itemKeyMap.put(key, lbl);
		}
		lbl.setText(title);
		breadcrumbPanel.add(lbl);
		lastLabel = lbl;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.rozlicz2.application.client.view.BreadcrumbView#
	 * changeBreadcrumbItemNameByKey(java.lang.String, java.lang.String)
	 */
	@Override
	public void changeBreadcrumbItemNameByKey(String key, String title) {
		Label item = itemKeyMap.get(key);

		if (item == null) {
			throw new RuntimeException("Item key not found!");
		}

		item.setText(title);

	}

	@Override
	public void clearBreadcrumbItems() {
		for (Label lbl : itemKeyMap.values()) {
			lbl.removeFromParent();
		}
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}
