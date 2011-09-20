package com.rozlicz2.application.shared.entity;

import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.Size;

public class DatastoreObject {
	@Id
	@Size(max = 32, min = 32)
	private String id;
	private Integer version = 0;

	public DatastoreObject() {
	}

	public DatastoreObject(DatastoreObject datastoreObject) {
		this.id = datastoreObject.id;
		this.version = datastoreObject.version;
	}

	public String getId() {
		return id;
	}

	public Integer getVersion() {
		return version;
	}

	@PrePersist
	void onPersist() {
		this.version++;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
