package com.rozlicz2.application.client.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.view.client.AbstractDataProvider;
import com.google.gwt.view.client.HasData;

public class IdArrayMap<T extends BaseEntity> extends AbstractDataProvider<T>
		implements IdMap<T> {

	Map<Long, T> map = new HashMap<Long, T>();

	@Override
	public void addDataDisplay(final HasData<T> display) {
		if (display == null) {
			throw new IllegalArgumentException("display cannot be null");
		}
		display.setRowCount(0, true);
		super.addDataDisplay(display);
	}

	@Override
	public boolean containsKey(Long key) {
		return map.containsKey(key);
	}

	@Override
	public T get(long id) {
		return map.get(new Long(id));
	}

	@Override
	public Set<Long> keySet() {
		return map.keySet();
	}

	@Override
	protected void onRangeChanged(HasData<T> display) {
		// TODO do a optimization
		List<T> entities = new ArrayList<T>();
		for (T entity : map.values()) {
			entities.add(entity);
		}
		updateRowData(display, 0, entities);
		int newRowCount = entities.size();
		int actualRowCount = display.getRowCount();
		if (newRowCount != actualRowCount)
			display.setRowCount(newRowCount, true);
	}

	@Override
	public void put(T o) {
		Long k = o.getId();
		map.put(k, o);
	}

	@Override
	public void remove(T o) {
		map.remove(o);
	}

	@Override
	public String toString() {
		return map.values().toString();
	}

	@Override
	public Collection<T> values() {
		return map.values();
	}

}
