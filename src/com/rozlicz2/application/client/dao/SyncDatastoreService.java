package com.rozlicz2.application.client.dao;

public interface SyncDatastoreService {
	public void put(SyncEntity entity);
	public SyncEntity get(SyncKey entity);
	public void delete(SyncKey... entity);
	public SyncPreparedQuery perepare(SyncQuery query);
	public void clean();
}
