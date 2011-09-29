package com.rozlicz2.application.shared.entity;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;

@Entity
public class Contact extends DatastoreObject {
	private String name;
	private Key<AppUser> owner;

	public String getName() {
		return name;
	}

	public Key<AppUser> getOwner() {
		return owner;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOwner(Key<AppUser> owner) {
		this.owner = owner;
	}
}
