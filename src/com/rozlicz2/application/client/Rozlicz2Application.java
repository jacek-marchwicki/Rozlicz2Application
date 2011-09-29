package com.rozlicz2.application.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;
import com.rozlicz2.application.client.mvp.AppGinjector;
import com.rozlicz2.application.client.resources.ApplicationResources;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Rozlicz2Application implements EntryPoint {

	/*
	 * private final GreetingServiceAsync greetingService = GWT
	 * .create(GreetingService.class);
	 */

	private final AppGinjector appGinjector = GWT.create(AppGinjector.class);;

	private void hideLoader() {
		Element loadingElement = DOM.getElementById("loading");
		Element parent = loadingElement.getParentElement();
		parent.removeChild(loadingElement);
	}

	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {
		ApplicationResources.INSTANCE.css().ensureInjected();

		Dashboard dashboard = appGinjector.getDashboard();
		RootPanel.get("application").add(dashboard);
		hideLoader();
	}
}
