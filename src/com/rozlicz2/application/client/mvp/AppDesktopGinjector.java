package com.rozlicz2.application.client.mvp;

import com.google.gwt.inject.client.GinModules;

@GinModules(value = { AppModule.class, AppViewsModule.class,
		AppActivititiesModule.class })
public interface AppDesktopGinjector extends AppGinjector {

}
