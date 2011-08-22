package com.rozlicz2.application.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.TextResource;

public interface ApplicationResources extends ClientBundle {
	public static final ApplicationResources INSTANCE =  GWT.create(ApplicationResources.class);

	@Source("application.css")
	public CssResource css();

	@Source("default.txt")
	public TextResource defaultText();
}
