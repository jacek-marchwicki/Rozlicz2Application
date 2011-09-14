package com.rozlicz2.application.client.dao;

public interface SyncObserver {
	public void changed(SyncEntity before, SyncEntity after);
}
