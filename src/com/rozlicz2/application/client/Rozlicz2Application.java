package com.rozlicz2.application.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.rozlicz2.application.client.resources.ApplicationResources;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Rozlicz2Application implements EntryPoint {

	private final GreetingServiceAsync greetingService = GWT
	.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		ApplicationResources.INSTANCE.css().ensureInjected();

		Dashboard dashboard = new Dashboard();
		RootPanel.get().add(dashboard);
	}
}
