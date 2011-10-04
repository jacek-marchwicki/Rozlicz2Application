package com.rozlicz2.application.client.mvp;

import com.google.gwt.inject.client.GinModules;

@GinModules(value = { AppModule.class, AppViewsMobileModule.class,
		AppActivititiesModule.class })
public interface AppMobileGinjector extends AppGinjector {

}
