package com.rozlicz2.application.client;

import com.google.gwt.view.client.ProvidesKey;
import com.rozlicz2.application.client.dao.SyncEntity;

public class EntityProvidesKey implements ProvidesKey<SyncEntity> {

	@Override
	public Object getKey(SyncEntity item) {
		return (item == null) ? null : item.getKey();
	}
}
