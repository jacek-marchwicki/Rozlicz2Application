package com.rozlicz2.application.client.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.HasData;

public class AbstractEntityProvider extends AbstractDataProvider<SyncEntity> {

	private final Iterable<SyncEntity> iterable;

	public AbstractEntityProvider(Iterable<SyncEntity> iterable) {
		this.iterable = iterable;
	}

	@Override
	protected void onRangeChanged(HasData<SyncEntity> display) {
		List<SyncEntity> entities = new ArrayList<SyncEntity>();
		for (SyncEntity entity : iterable) {
			entities.add(entity);
		}
		if (entities.size() != 0) {
			updateRowData(display, 0, entities);
		}
	}

}
