package com.rozlicz2.application.client.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;

public class SyncDatastoreServiceImpl implements SyncDatastoreService {

	private static class StoreEntity {
		private SyncEntity entity;
		private List<SyncPropertyKey> propertyKeys;

		public SyncEntity getEntity() {
			return entity;
		}

		public List<SyncPropertyKey> getPropertyKeys() {
			return propertyKeys;
		}

		public void setEntity(SyncEntity entity) {
			this.entity = entity;
		}

		public void setPropertyKeys(List<SyncPropertyKey> propertyKeys) {
			this.propertyKeys = propertyKeys;
		}
	}

	private static class SyncPreparedQueryImpl implements SyncPreparedQuery {

		private final Map<SyncKey, StoreEntity> entities;
		private final TreeMap<SyncPropertyKey, SyncKey> properties;
		private final SyncQuery query;

		public SyncPreparedQueryImpl(SyncQuery query,
				Map<SyncKey, StoreEntity> entities,
				TreeMap<SyncPropertyKey, SyncKey> properties) {
			this.query = query;
			this.entities = entities;
			this.properties = properties;
		}

		@Override
		public AbstractEntityProvider asDataProvider() {
			return new AbstractEntityProvider(asIterable());
		}

		@Override
		public Iterable<SyncEntity> asIterable() {
			return new SyncQueryIterable(query, entities, properties);
		}

		@Override
		public SyncEntity asSingleEntity() {
			Iterable<SyncEntity> asIterable = this.asIterable();
			Iterator<SyncEntity> iterator = asIterable.iterator();
			if (!iterator.hasNext())
				return null;
			return iterator.next();
		}
	}

	private static class SyncQueryIterable implements Iterable<SyncEntity> {

		private final Map<SyncKey, StoreEntity> entities;
		private final TreeMap<SyncPropertyKey, SyncKey> properties;
		private final SyncQuery query;

		public SyncQueryIterable(SyncQuery query,
				Map<SyncKey, StoreEntity> entities,
				TreeMap<SyncPropertyKey, SyncKey> properties) {
			this.query = query;
			this.entities = entities;
			this.properties = properties;
		}

		@Override
		public Iterator<SyncEntity> iterator() {
			return new SyncQueryIterator(query, entities, properties);
		}

	}

	private static class SyncQueryIterator implements Iterator<SyncEntity> {

		private final Map<SyncKey, StoreEntity> entities;
		boolean hasNextItem;
		private Iterator<SyncPropertyKey> iterator;
		private final String kind;
		private SyncPropertyKey nextKey;

		private final TreeMap<SyncPropertyKey, SyncKey> properties;
		private final SyncQuery query;

		private SyncPropertyKey validator;

		public SyncQueryIterator(SyncQuery query,
				Map<SyncKey, StoreEntity> entities,
				TreeMap<SyncPropertyKey, SyncKey> properties) {
			this.query = query;
			this.entities = entities;
			this.properties = properties;
			this.kind = query.getKind();

			createStartKey();
			isValidNextKey();
		}

		private void calculateNextKey() {
			if (iterator.hasNext()) {
				nextKey = iterator.next();
			} else {
				nextKey = null;
			}
		}

		private void createStartKey() {
			Collection<SyncQuery.Filter> filters = this.query.getFilters();
			SyncPropertyKey propertyKey;
			if (filters.size() == 0) {
				propertyKey = new SyncPropertyKey(kind);
				validator = propertyKey;

			} else if (filters.size() == 1) {
				Iterator<SyncQuery.Filter> iterator = filters.iterator();
				SyncQuery.Filter filter = iterator.next();
				SyncQuery.FilterOperator operator = filter.getOperator();
				if (!operator.equals(SyncQuery.FilterOperator.EQUAL)) {
					throw new RuntimeException("Not implemented");
					// TODO
				}
				String propertyName = filter.getPropertyName();
				Object value = filter.getValue();
				propertyKey = new SyncPropertyKey(kind, propertyName, value);
				validator = propertyKey;
			} else {
				throw new RuntimeException("Not implemented");
				// TODO
			}
			SortedMap<SyncPropertyKey, SyncKey> subMap;
			subMap = properties.tailMap(propertyKey);
			iterator = subMap.keySet().iterator();
			if (iterator.hasNext()) {
				nextKey = iterator.next();
			}
		}

		@Override
		public boolean hasNext() {
			return hasNextItem;
		}

		private void isValidNextKey() {
			if (nextKey == null) {
				hasNextItem = false;
				return;
			}
			if (validator == null) {
				throw new RuntimeException("Not implemented");
				// TODO
			}
			boolean contains = validator.contains(nextKey) == 0 ? true : false;
			hasNextItem = contains;
		}

		@Override
		public SyncEntity next() {
			if (hasNextItem == false)
				throw new NoSuchElementException();
			assert (nextKey != null);

			SyncKey key = properties.get(nextKey);
			assert (key != null);

			StoreEntity storeEntity = entities.get(key);
			assert (storeEntity != null);

			SyncEntity entity = storeEntity.getEntity();
			assert (entity != null);

			calculateNextKey();
			isValidNextKey();

			return entity.cloneMe();
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub
		}

	}

	private final Map<SyncKey, StoreEntity> entities = new HashMap<SyncKey, StoreEntity>();
	private final Map<String, List<SyncObserver>> observers = new HashMap<String, List<SyncObserver>>();

	private final TreeMap<SyncPropertyKey, SyncKey> properties = new TreeMap<SyncPropertyKey, SyncKey>();

	private final Map<String, Map<String, List<SyncObserver>>> propertyObservers = new HashMap<String, Map<String, List<SyncObserver>>>();

	@Override
	public void addObserver(String kind, SyncObserver observer) {
		List<SyncObserver> list = observers.get(kind);
		if (list == null) {
			list = new ArrayList<SyncObserver>();
			observers.put(kind, list);
		}
		list.add(observer);
	}

	@Override
	public void addPropertyObserver(String kind, String property,
			SyncObserver observer) {
		Map<String, List<SyncObserver>> map = propertyObservers.get(kind);
		if (map == null) {
			map = new HashMap<String, List<SyncObserver>>();
			propertyObservers.put(kind, map);
		}
		List<SyncObserver> list = map.get(property);
		if (list == null) {
			list = new ArrayList<SyncObserver>();
			map.put(property, list);
		}
		list.add(observer);

	}

	@Override
	public void clean() {
		entities.clear();
		properties.clear();
	}

	/**
	 * Create key for property value
	 * 
	 * @param key
	 *            entity key
	 * @param propertyKeys
	 *            variable storing created property keys
	 * @param propertyName
	 *            property name
	 * @param value
	 *            property value
	 */
	private void createPropertyKey(SyncKey key,
			List<SyncPropertyKey> propertyKeys, String propertyName,
			Object value) {
		SyncPropertyKey syncPropertyKey = new SyncPropertyKey(key,
				propertyName, value);
		if (properties.containsKey(syncPropertyKey)) {
			// Do not create duplicate keys
			return;
		}
		properties.put(syncPropertyKey, key);
		propertyKeys.add(syncPropertyKey);
	}

	/**
	 * Create keys for each property collection value
	 * 
	 * @param key
	 *            entity key
	 * @param propertyKeys
	 *            variable storing created property keys
	 * @param propertyName
	 *            property name
	 * @param values
	 *            property entity value (collection)
	 */
	private void createPropertyKeyList(SyncKey key,
			List<SyncPropertyKey> propertyKeys, String propertyName,
			Collection<?> values) {
		for (Object value : values) {
			createPropertyKey(key, propertyKeys, propertyName, value);
		}
	}

	private void createPropertyKeys(StoreEntity storeEntity, SyncEntity entity) {
		SyncKey key = entity.getKey();

		List<SyncPropertyKey> propertyKeys = new ArrayList<SyncPropertyKey>();
		storeEntity.setPropertyKeys(propertyKeys);

		for (String propertyName : entity.getProperties()) {
			Object value = entity.getProperty(propertyName);
			if (value instanceof Collection<?>) {
				createPropertyKeyList(key, propertyKeys, propertyName,
						(Collection<?>) value);
			} else {
				createPropertyKey(key, propertyKeys, propertyName, value);
			}
		}
	}

	@Override
	public void delete(SyncKey... keys) {
		for (SyncKey key : keys) {
			StoreEntity storeEntity = entities.get(key);
			if (storeEntity != null) {
				deletePropertyKeys(storeEntity);
			}
			entities.remove(key);
			if (storeEntity != null) {
				SyncEntity entity = storeEntity.getEntity();
				assert (entity != null);
				fireObservers(entity, null);
			}
		}
	}

	private void deletePropertyKeys(StoreEntity storeEntity) {
		for (SyncPropertyKey propertyKey : storeEntity.getPropertyKeys()) {
			properties.remove(propertyKey);
		}
	}

	private void fireAllPropertyObservers(SyncEntity before, SyncEntity after,
			Map<String, List<SyncObserver>> map) {
		for (List<SyncObserver> observerList : map.values()) {
			for (SyncObserver syncObserver : observerList) {
				syncObserver.changed(before, after);
			}
		}
	}

	private void fireChangedPropertyObservers(SyncEntity before,
			SyncEntity after, Map<String, List<SyncObserver>> map) {
		Collection<String> changedProperties = after.getChangedProperties();
		for (String changedPropertyName : changedProperties) {
			List<SyncObserver> list2 = map.get(changedPropertyName);
			if (list2 != null) {
				for (SyncObserver syncObserver : list2) {
					syncObserver.changed(before, after);
				}
			}
		}
	}

	private void fireObservers(SyncEntity before, SyncEntity after) {
		String kind = null;
		if (before != null)
			kind = before.getKey().getKind();
		else if (after != null)
			kind = after.getKey().getKind();
		assert (kind != null);
		List<SyncObserver> list = observers.get(kind);
		if (list != null) {
			for (SyncObserver observer : list) {
				observer.changed(before, after);
			}
		}
		firePropertyObservers(kind, before, after);
	}

	private void firePropertyObservers(String kind, SyncEntity before,
			SyncEntity after) {
		Map<String, List<SyncObserver>> map = propertyObservers.get(kind);
		if (map != null) {
			if (before == null || after == null) {
				fireAllPropertyObservers(before, after, map);
			} else {
				fireChangedPropertyObservers(before, after, map);
			}
		}
	}

	@Override
	public SyncEntity get(SyncKey key) {
		StoreEntity syncEntity = entities.get(key);
		return (syncEntity == null ? null : syncEntity.getEntity().cloneMe());
	}

	@Override
	public SyncPreparedQuery prepare(SyncQuery query) {
		return new SyncPreparedQueryImpl(query, entities, properties);
	}

	@Override
	public void put(SyncEntity entity) {
		SyncKey key = entity.getKey();
		StoreEntity storeEntity = entities.get(key);
		if (storeEntity != null)
			deletePropertyKeys(storeEntity);
		if (storeEntity == null)
			storeEntity = new StoreEntity();
		SyncEntity old = storeEntity.getEntity();
		storeEntity.setEntity(entity.cloneMe());
		createPropertyKeys(storeEntity, entity);
		entities.put(key, storeEntity);
		fireObservers(old, entity);
	}

	@Override
	public void removeObserver(String entity, SyncObserver observer) {
		List<SyncObserver> list = observers.get(entity);
		if (list == null)
			return;
		list.remove(observer);
	}

	@Override
	public void removePropertyObserver(String entity, String property,
			SyncObserver observer) {
		Map<String, List<SyncObserver>> map = propertyObservers.get(entity);
		if (map == null) {
			return;
		}
		List<SyncObserver> list = map.get(property);
		if (list == null) {
			return;
		}
		list.remove(observer);
	}

}
