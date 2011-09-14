package com.rozlicz2.application.client.dao;

public interface SyncPreparedQuery {

	AbstractEntityProvider asDataProvider();

	Iterable<SyncEntity> asIterable();

	SyncEntity asSingleEntity();

}
