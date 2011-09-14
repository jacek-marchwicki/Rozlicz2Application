package com.rozlicz2.application.client.entity;

import java.util.Collection;
import java.util.Set;

import com.google.gwt.view.client.HasData;

public interface IdMap<T extends BaseEntity> {
	void addDataDisplay(HasData<T> display);

	boolean containsKey(Long key);

	public T get(long id);

	public Set<HasData<T>> getDataDisplays();

	public Set<Long> keySet();

	public void put(T o);

	public void remove(T o);

	void removeDataDisplay(HasData<T> display);

	public Collection<T> values();
}
