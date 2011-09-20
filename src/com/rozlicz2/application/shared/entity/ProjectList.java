package com.rozlicz2.application.shared.entity;

import javax.validation.constraints.Size;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;

@Entity
public class ProjectList extends DatastoreObject {

	@Size(min = 2)
	private String name;
	private Key<AppUser> owner;

	public ProjectList() {
	}

	public ProjectList(ProjectList project) {
		super(project);
		this.name = project.name;
		this.owner = project.owner;
	}

	public String getName() {
		return name;
	}

	public Key<AppUser> getOwner() {
		return owner;
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
