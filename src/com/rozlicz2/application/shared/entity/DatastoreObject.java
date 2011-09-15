package com.rozlicz2.application.shared.entity;

import javax.persistence.Id;
import javax.persistence.PrePersist;

public class DatastoreObject {
	@Id
	private Long id;
	private Integer version = 0;

	public Long getId() {
		return id;
	}

	public Integer getVersion() {
		return version;
	}

	@PrePersist
	void onPersist() {
		this.version++;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
