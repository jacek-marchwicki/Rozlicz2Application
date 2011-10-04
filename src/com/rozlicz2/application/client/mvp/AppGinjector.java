package com.rozlicz2.application.client.mvp;

import com.google.gwt.inject.client.Ginjector;
import com.rozlicz2.application.client.Starter;

public interface AppGinjector extends Ginjector {
	Starter getMain();
}
