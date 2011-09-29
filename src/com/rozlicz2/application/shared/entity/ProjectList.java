package com.rozlicz2.application.shared.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.rozlicz2.application.shared.validator.ServerGroup;

@Entity
public class ProjectList extends DatastoreObject {

	@NotNull
	@Size(min = 4)
	private String name;

	@NotNull(groups = { ServerGroup.class })
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

	public void setOwner(Key<AppUser> owner) {
		this.owner = owner;
	}
}
