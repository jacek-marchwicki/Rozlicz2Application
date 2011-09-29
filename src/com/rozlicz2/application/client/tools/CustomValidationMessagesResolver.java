package com.rozlicz2.application.client.tools;

import com.google.gwt.core.client.GWT;
import com.google.gwt.validation.client.AbstractValidationMessageResolver;
import com.google.gwt.validation.client.ProviderValidationMessageResolver;
import com.google.gwt.validation.client.UserValidationMessagesResolver;
import com.rozlicz2.application.client.resources.ValidationMessages;

public class CustomValidationMessagesResolver extends
		AbstractValidationMessageResolver implements
		UserValidationMessagesResolver, ProviderValidationMessageResolver {

	private static final ValidationMessages validationMessages = GWT
			.create(ValidationMessages.class);

	protected CustomValidationMessagesResolver() {
		super(validationMessages);
	}

}
