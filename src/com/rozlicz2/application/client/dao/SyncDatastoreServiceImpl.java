package com.rozlicz2.application.client.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
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
		private final String kind;
		private Entry<SyncPropertyKey, SyncKey> nextKey;

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
			nextKey = properties.higherEntry(nextKey.getKey());
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
			nextKey = properties.ceilingEntry(propertyKey);
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
			boolean contains = validator.contains(nextKey.getKey()) == 0 ? true
					: false;
			hasNextItem = contains;
		}

		@Override
		public SyncEntity next() {
			if (hasNextItem == false)
				throw new NoSuchElementException();
			assert (nextKey != null);

			SyncKey key = nextKey.getValue();
			assert (key != null);

			StoreEntity storeEntity = entities.get(key);
			assert (storeEntity != null);

			SyncEntity entity = storeEntity.getEntity();
			assert (entity != null);

			calculateNextKey();
			isValidNextKey();

			return entity.clone();
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub
		}

	}

	private final Map<SyncKey, StoreEntity> entities = new HashMap<SyncKey, StoreEntity>();
	private final TreeMap<SyncPropertyKey, SyncKey> properties = new TreeMap<SyncPropertyKey, SyncKey>();

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
			if (storeEntity != null)
				deletePropertyKeys(storeEntity);
			entities.remove(key);
		}
	}

	private void deletePropertyKeys(StoreEntity storeEntity) {
		for (SyncPropertyKey propertyKey : storeEntity.getPropertyKeys()) {
			properties.remove(propertyKey);
		}
	}

	@Override
	public SyncEntity get(SyncKey key) {
		StoreEntity syncEntity = entities.get(key);
		return syncEntity == null ? null : syncEntity.getEntity().clone();
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
		storeEntity = new StoreEntity();
		storeEntity.setEntity(entity.clone());
		createPropertyKeys(storeEntity, entity);
		entities.put(key, storeEntity);
	}

}
