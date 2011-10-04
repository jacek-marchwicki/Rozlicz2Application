package com.rozlicz2.application.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;
import com.rozlicz2.application.client.mvp.AppDesktopGinjector;
import com.rozlicz2.application.client.mvp.AppGinjector;
import com.rozlicz2.application.client.mvp.AppMobileGinjector;
import com.rozlicz2.application.client.resources.ApplicationResources;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Rozlicz2Application implements EntryPoint {

	public static interface MyApp {
		public void Start(RootPanel rootPanel);
	}

	public static class MyAppDesktop implements MyApp {
		private final AppGinjector appGinjector = GWT
				.create(AppDesktopGinjector.class);

		@Override
		public void Start(RootPanel rootPanel) {
			// TODO Auto-generated method stub
			Starter main = appGinjector.getMain();
			main.start(rootPanel);
		}
	}

	public static class MyAppMobile implements MyApp {
		private final AppGinjector appGinjector = GWT
				.create(AppMobileGinjector.class);

		@Override
		public void Start(RootPanel rootPanel) {
			// TODO Auto-generated method stub
			Starter main = appGinjector.getMain();
			main.start(rootPanel);
		}
	}

	MyApp myApp = GWT.create(MyApp.class);

	private void hideLoader() {
		Element loadingElement = DOM.getElementById("loading");
		Element parent = loadingElement.getParentElement();
		parent.removeChild(loadingElement);
	}

	@Override
	public void onModuleLoad() {
		ApplicationResources.INSTANCE.css().ensureInjected();

		myApp.Start(RootPanel.get("application"));

		hideLoader();
	}
}
