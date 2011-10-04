package com.rozlicz2.application.client.widgets;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.i18n.client.NumberFormat;

public class CurrencyWidget extends EditableUniversalWidget<Double> {

	@Override
	protected Double convertFromString(String string) {
		if (string == null)
			return null;
		Double value = null;
		try {
			value = NumberFormat.getDecimalFormat().parse(string);

		} catch (NumberFormatException ex) {
			delegate.recordError("Wrong decimal value", string, string);
		}
		return value;
	}

	@Override
	protected String convertToEditableText(Double value) {
		return NumberFormat.getDecimalFormat().format(value);
	}

	@Override
	protected String convertToText(Double value) {
		return NumberFormat.getCurrencyFormat().format(value);
	}

	@Override
	protected void extendedOnKeyPressEvent(KeyPressEvent e) {
		char charCode = e.getCharCode();
		if (charCode == ',')
			return;
		if (charCode == '.')
			return;
		if (charCode >= '0' && charCode <= '9')
			return;
		e.preventDefault();
	}
}
