package com.rozlicz2.application.shared.entity;

import java.util.List;

import javax.persistence.Embedded;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;

@Entity
public class ProjectList extends DatastoreObject {
	@Embedded
	private List<ListItem> items;
	private String name;
	private Key<AppUser> owner;

	public ProjectList() {
	}

	public List<ListItem> getItems() {
		return items;
	}

	public String getName() {
		return name;
	}

	public Key<AppUser> getOwner() {
		return owner;
	}

	public void setItems(List<ListItem> items) {
		this.items = items;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOwner(AppUser owner) {
		this.owner = owner.getKey();
	}

	public void setOwner(Key<AppUser> owner) {
		this.owner = owner;
	}

}
