package com.rozlicz2.application.shared.entity;

import java.util.Date;

public class ListItem {
	private Date dateCreated;
	private String itemText;

	public Date getDateCreated() {
		return dateCreated;
	}

	public String getItemText() {
		return itemText;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setItemText(String itemText) {
		this.itemText = itemText;
	}

}
