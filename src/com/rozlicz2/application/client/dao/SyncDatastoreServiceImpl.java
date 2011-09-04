package com.rozlicz2.application.client.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SyncDatastoreServiceImpl implements SyncDatastoreService {
	
	private static class SyncQueryIterator implements Iterator<SyncEntity> {

		@Override
		public boolean hasNext() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public SyncEntity next() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private static class SyncQueryIterable implements Iterable<SyncEntity> {

		@Override
		public Iterator<SyncEntity> iterator() {
			return new SyncQueryIterator();
		}
		
	}
	
	private static class SyncPreparedQueryImpl implements SyncPreparedQuery {

		public SyncPreparedQueryImpl(SyncQuery query) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public Iterable<SyncEntity> asIterable() {
			return new SyncQueryIterable();
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
			SyncPropertyKey syncPropertyKey = new SyncPropertyKey(key, propertyName, value);
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
		return new SyncPreparedQueryImpl(query);
	}

	@Override
	public void clean() {
		entities.clear();
	}

}
