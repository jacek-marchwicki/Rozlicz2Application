package com.rozlicz2.application.client.dao;

public interface BaseDAO {
	void addListener(DAOListener listener);
	void removeListener(DAOListener listener);
}
