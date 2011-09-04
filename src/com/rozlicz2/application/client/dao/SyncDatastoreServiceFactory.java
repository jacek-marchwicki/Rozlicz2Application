package com.rozlicz2.application.client.dao;

public class SyncDatastoreServiceFactory {
	private static SyncDatastoreService datastoreService = new SyncDatastoreServiceImpl();
	public static SyncDatastoreService getDatastoreService() {
		return datastoreService;
	}
}
