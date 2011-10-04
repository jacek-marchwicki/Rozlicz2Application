package com.rozlicz2.application.client.widgets;

public class EditableTextWidget extends EditableUniversalWidget<String> {

	@Override
	protected String convertFromString(String string) {
		return string;
	}

	@Override
	protected String convertToText(String value) {
		return value;
	}

}
