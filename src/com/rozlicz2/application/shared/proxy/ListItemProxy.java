package com.rozlicz2.application.shared.proxy;

import java.util.Date;

import com.google.web.bindery.requestfactory.shared.ProxyFor;
import com.google.web.bindery.requestfactory.shared.ValueProxy;
import com.rozlicz2.application.shared.entity.ListItem;


@ProxyFor(value = ListItem.class)
public interface ListItemProxy extends ValueProxy
{
	String getItemText();
	void setItemText(String itemText);
	Date getDateCreated();
}