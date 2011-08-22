package com.rozlicz2.application.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface ErrorMessages extends Messages {
	static ErrorMessages messages = GWT.create(ErrorMessages.class);
}