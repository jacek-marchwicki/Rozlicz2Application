package com.rozlicz2.application.client.dao;

public class SyncKeyFactory {

	public static SyncKey createKey(String kind, String key) {
		long id = (long) key.hashCode();
		SyncKey syncKey = new SyncKey(kind, id);
		return syncKey;
	}

}
