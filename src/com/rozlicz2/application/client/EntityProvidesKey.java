package com.rozlicz2.application.client;

import com.google.gwt.view.client.ProvidesKey;
import com.rozlicz2.application.client.dao.BaseEntity;

public class EntityProvidesKey<T extends BaseEntity> implements ProvidesKey<T> {

	@Override
	public Object getKey(BaseEntity item) {
		return (item == null) ? null : item.getId();
	}

}
