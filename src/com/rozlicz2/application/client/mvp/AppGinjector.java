package com.rozlicz2.application.client.mvp;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.rozlicz2.application.client.Dashboard;

@GinModules(value = { AppModule.class, AppViewsModule.class })
public interface AppGinjector extends Ginjector {
	Dashboard getDashboard();
}
