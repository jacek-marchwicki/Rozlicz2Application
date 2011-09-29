package com.rozlicz2.application.server.locator;

import com.google.web.bindery.requestfactory.shared.Locator;
import com.rozlicz2.application.server.service.ObjectifyDao;
import com.rozlicz2.application.shared.entity.DatastoreObject;

/**
 * Generic @Locator for objects that extend DatastoreObject
 */
public class ObjectifyLocator extends Locator<DatastoreObject, String> {
	@Override
	public DatastoreObject create(Class<? extends DatastoreObject> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public DatastoreObject find(Class<? extends DatastoreObject> clazz,
			String id) {
		/**
		 * INFO - debug sleep: try { Thread.sleep(1000); } catch
		 * (InterruptedException e1) { e1.printStackTrace(); }
		 */

		@SuppressWarnings("rawtypes")
		ObjectifyDao daoBase = new ObjectifyDao();
		return daoBase.ofy().find(clazz, id);
	}

	@Override
	public Class<DatastoreObject> getDomainType() {
		// Never called
		return null;
	}

	@Override
	public String getId(DatastoreObject domainObject) {
		return domainObject.getId();
	}

	@Override
	public Class<String> getIdType() {
		return String.class;
	}

	@Override
	public Object getVersion(DatastoreObject domainObject) {
		return domainObject.getVersion();
	}
}