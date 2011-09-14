package com.rozlicz2.application.client.dao;

public interface SyncDatastoreService {
	public void addObserver(String kind, SyncObserver observer);

	public void addPropertyObserver(String kind, String property,
			SyncObserver observer);

	public void clean();

	public void delete(SyncKey... entity);

	public SyncEntity get(SyncKey entity);

	public SyncPreparedQuery prepare(SyncQuery query);

	public void put(SyncEntity entity);

	public void removeObserver(String entity, SyncObserver observer);

	public void removePropertyObserver(String entity, String property,
			SyncObserver observer);
}
