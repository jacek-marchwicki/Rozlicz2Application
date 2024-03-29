package com.rozlicz2.application.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * i18n for normal messages documentation:
 * http://code.google.com/webtoolkit/doc/latest/DevGuideI18nMessages.html
 * 
 * @author Jacek Marchwicki
 * 
 */
public interface LocalizedMessages extends Messages {
	static LocalizedMessages messages = GWT.create(LocalizedMessages.class);

	@DefaultMessage("Number of projects is {0}")
	String numberOfProjects(int numberOfProjects);

	@DefaultMessage("Payments sum: {0}")
	String paymentsSum(Double sum);

	@DefaultMessage("Price: <b>{0,number,currency}</b>")
	SafeHtml price(Double sum);

	@DefaultMessage("Hello: {0}")
	String sayHello(String name);

	@DefaultMessage("<b>Server replies:</b> {0}")
	SafeHtml serverReplies(SafeHtml serverResponseLabel);
}
