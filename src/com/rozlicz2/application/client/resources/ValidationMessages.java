package com.rozlicz2.application.client.resources;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface ValidationMessages extends ConstantsWithLookup {
	@DefaultStringValue("Internal error")
	@Key("custom.internal.error")
	String custom_internal_error();

	@DefaultStringValue("Name must be at least {min} characters long. (Max {max} characters)")
	@Key("custom.name.size.message")
	String custom_name_size_message();
}
