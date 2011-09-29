package com.rozlicz2.application.client.resources;

import com.google.gwt.i18n.client.ConstantsWithLookup;

public interface ValidationMessages extends ConstantsWithLookup {
	@DefaultStringValue("Name must be at least {min} characters long.")
	@Key("custom.name.size.message")
	String custom_name_size_message();
}
