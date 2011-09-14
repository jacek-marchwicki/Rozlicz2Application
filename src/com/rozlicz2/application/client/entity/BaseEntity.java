package com.rozlicz2.application.client.entity;

import com.google.gwt.view.client.ProvidesKey;

public abstract class BaseEntity {
	public static class EntityKeyProvider<T extends BaseEntity> implements
			ProvidesKey<T> {

		@Override
		public Object getKey(BaseEntity item) {
			return (item == null) ? null : item.id;
		}
	}

	private long id;

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof BaseEntity))
			return false;
		BaseEntity k = (BaseEntity) o;
		return id == k.id ? true : false;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
