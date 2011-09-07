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

	private static class SyncQueryIterator implements Iterator<SyncEntity> {

		private final SyncQuery query;
		private final Map<SyncKey, StoreEntity> entities;
		private final TreeMap<SyncPropertyKey, SyncKey> properties;
		private final String kind;

		private Entry<SyncPropertyKey, SyncKey> nextKey;
		boolean hasNextItem;

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
		
		private SyncPropertyKey validator;

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

		private void calculateNextKey() {
			nextKey = properties.higherEntry(nextKey.getKey());
		}

		private void isValidNextKey() {
			if (nextKey == null)
			{
				hasNextItem = false;
				return;
			}
			if (validator == null) {
				throw new RuntimeException("Not implemented");
				// TODO
			}
			boolean contains = validator.contains(nextKey.getKey()) == 0 ? true : false;
			hasNextItem = contains;
		}
		
		@Override
		public boolean hasNext() {
			return hasNextItem;
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

	private static class SyncQueryIterable implements Iterable<SyncEntity> {

		private final SyncQuery query;
		private final Map<SyncKey, StoreEntity> entities;
		private final TreeMap<SyncPropertyKey, SyncKey> properties;

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

	private static class SyncPreparedQueryImpl implements SyncPreparedQuery {

		private final SyncQuery query;
		private final Map<SyncKey, StoreEntity> entities;
		private final TreeMap<SyncPropertyKey, SyncKey> properties;

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

	private static class StoreEntity {
		private SyncEntity entity;
		private List<SyncPropertyKey> propertyKeys;

		public void setPropertyKeys(List<SyncPropertyKey> propertyKeys) {
			this.propertyKeys = propertyKeys;
		}

		public List<SyncPropertyKey> getPropertyKeys() {
			return propertyKeys;
		}

		public void setEntity(SyncEntity entity) {
			this.entity = entity;
		}

		public SyncEntity getEntity() {
			return entity;
		}
	}

	private Map<SyncKey, StoreEntity> entities = new HashMap<SyncKey, StoreEntity>();
	private TreeMap<SyncPropertyKey, SyncKey> properties = new TreeMap<SyncPropertyKey, SyncKey>();

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

	private void createPropertyKeys(StoreEntity storeEntity, SyncEntity entity) {
		SyncKey key = entity.getKey();
		List<SyncPropertyKey> propertyKeys = new ArrayList<SyncPropertyKey>();
		for (String propertyName : entity.getProperties()) {
			Object value = entity.getProperty(propertyName);
			SyncPropertyKey syncPropertyKey = new SyncPropertyKey(key,
					propertyName, value);
			properties.put(syncPropertyKey, key);
			propertyKeys.add(syncPropertyKey);
		}
		storeEntity.setPropertyKeys(propertyKeys);
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
	public void delete(SyncKey... keys) {
		for (SyncKey key : keys) {
			StoreEntity storeEntity = entities.get(key);
			deletePropertyKeys(storeEntity);
			entities.remove(key);
		}
	}

	@Override
	public SyncPreparedQuery perepare(SyncQuery query) {
		return new SyncPreparedQueryImpl(query, entities, properties);
	}

	@Override
	public void clean() {
		entities.clear();
		properties.clear();
	}

}
