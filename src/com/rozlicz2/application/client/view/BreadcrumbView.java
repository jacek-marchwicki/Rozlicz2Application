package com.rozlicz2.application.client.view;

import com.google.gwt.user.client.ui.IsWidget;

public interface BreadcrumbView extends IsWidget {

	public interface Presenter {

		void gotoKey(String key);

	}

	public abstract void addBreadcrumbItem(String key, String title);

	public abstract void changeBreadcrumbItemNameByKey(String key, String title);

	public abstract void clearBreadcrumbItems();

	public void setPresenter(Presenter presenter);

}