package com.rozlicz2.application.client.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SyncEntity implements Serializable, Cloneable {
	private static final RandomString random = new RandomString(10);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final SyncKey key;
	private final Map<String, Object> properites = new HashMap<String, Object>();

	public SyncEntity(String kind) {
		String stringKey = random.nextString();
		key = SyncKeyFactory.createKey(kind, stringKey);
	}

	public SyncEntity(SyncKey key) {
		this.key = key;
	}

	public SyncEntity cloneMe() {
		SyncEntity entity = new SyncEntity(key);
		Iterator<String> iterator = properites.keySet().iterator();

		while (iterator.hasNext()) {
			String propertyKey = iterator.next();
			Object propertyValue = properites.get(propertyKey);
			entity.properites.put(propertyKey, propertyValue);
		}
		return entity;
	}

	public SyncKey getKey() {
		return key;
	}

	public Collection<String> getProperties() {
		return properites.keySet();
	}

	public Object getProperty(String propertyName) {
		return properites.get(propertyName);
	}

	public void removeProperty(String propertyName) {
		properites.remove(propertyName);
	}

	public void setProperty(String propertyName, Object value) {
		properites.put(propertyName, value);
	}
}
