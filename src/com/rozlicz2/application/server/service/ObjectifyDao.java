package com.rozlicz2.application.server.service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.Embedded;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.PreparedQuery.TooManyResultsException;
import com.google.web.bindery.requestfactory.server.RequestFactoryServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.util.DAOBase;
import com.rozlicz2.application.server.Rozlicz2UserService;
import com.rozlicz2.application.shared.entity.AppUser;

/**
 * Generic DAO for use with Objectify
 * 
 * @author turbomanage
 * 
 * @param <T>
 */
public class ObjectifyDao<T> extends DAOBase {

	static final int BAD_MODIFIERS = Modifier.FINAL | Modifier.STATIC
			| Modifier.TRANSIENT;

	private static Rozlicz2UserService userService = new Rozlicz2UserService();

	protected Class<T> clazz;

	@SuppressWarnings("unchecked")
	public ObjectifyDao() {
		Type genericSuperclass = getClass().getGenericSuperclass();
		// Allow this class to be safely instantiated with or without a
		// parameterized type
		if (genericSuperclass instanceof ParameterizedType)
			clazz = (Class<T>) ((ParameterizedType) genericSuperclass)
					.getActualTypeArguments()[0];
	}

	protected Query<T> buildQueryByExample(T exampleObj) {
		Query<T> q = ofy().query(clazz);

		// Add all non-null properties to query filter
		for (Field field : clazz.getDeclaredFields()) {
			// Ignore transient, embedded, array, and collection properties
			if (field.isAnnotationPresent(Transient.class)
					|| (field.isAnnotationPresent(Embedded.class))
					|| (field.getType().isArray())
					|| (field.getType().isArray())
					|| (Collection.class.isAssignableFrom(field.getType()))
					|| ((field.getModifiers() & BAD_MODIFIERS) != 0))
				continue;

			field.setAccessible(true);

			Object value;
			try {
				value = field.get(exampleObj);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			if (value != null) {
				q.filter(field.getName(), value);
			}
		}

		return q;
	}

	public void delete(T entity) {
		ofy().delete(entity);
	}

	public void deleteAll(Iterable<T> entities) {
		ofy().delete(entities);
	}

	public void deleteKey(Key<T> entityKey) {
		ofy().delete(entityKey);
	}

	public void deleteKeys(Iterable<Key<T>> keys) {
		ofy().delete(keys);
	}

	public T find(String id) {
		if (id == null)
			return null;
		T elem = ofy().find(clazz, id);
		return elem;
	}

	public Map<Key<T>, T> get(Iterable<Key<T>> keys) {
		return ofy().get(keys);
	}

	public T get(Key<T> key) throws NotFoundException {
		return ofy().get(key);
	}

	public T get(Long id) throws NotFoundException {
		return ofy().get(this.clazz, id);
	}

	public T getByExample(T exampleObj) throws TooManyResultsException {
		Query<T> q = buildQueryByExample(exampleObj);
		Iterator<T> fetch = q.limit(2).list().iterator();
		if (!fetch.hasNext()) {
			return null;
		}
		T obj = fetch.next();
		if (fetch.hasNext()) {
			throw new TooManyResultsException();
		}
		return obj;
	}

	/**
	 * Convenience method to get all objects matching a single property
	 * 
	 * @param propName
	 * @param propValue
	 * @return T matching Object
	 * @throws TooManyResultsException
	 */
	public T getByProperty(String propName, Object propValue)
			throws TooManyResultsException {
		Query<T> q = ofy().query(clazz);
		q.filter(propName, propValue);
		Iterator<T> fetch = q.limit(2).list().iterator();
		if (!fetch.hasNext()) {
			return null;
		}
		T obj = fetch.next();
		if (fetch.hasNext()) {
			throw new TooManyResultsException();
		}
		return obj;
	}

	protected AppUser getCurrentUser() {
		HttpServletRequest request = RequestFactoryServlet
				.getThreadLocalRequest();
		return userService.getCurrentUserInfo(request);
	}

	public Key<T> getKey(Long id) {
		return new Key<T>(this.clazz, id);
	}

	public Key<T> key(T obj) {
		return ObjectifyService.factory().getKey(obj);
	}

	public List<T> listAll() {
		Query<T> q = ofy().query(clazz);
		return q.list();
	}

	/*
	 * Application-specific methods to retrieve items owned by a specific user
	 */
	public List<T> listAllForUser() {
		AppUser currentUserInfo = getCurrentUser();
		if (currentUserInfo == null)
			return null;
		Key<AppUser> userKey = currentUserInfo.getKey();
		return listByProperty("owner", userKey);
	}

	public List<T> listByExample(T exampleObj) {
		Query<T> queryByExample = buildQueryByExample(exampleObj);
		return queryByExample.list();
	}

	public List<T> listByProperty(String propName, Object propValue) {
		Query<T> q = ofy().query(clazz);
		q.filter(propName, propValue);
		return q.list();
	}

	public List<Key<T>> listChildKeys(Object parent) {
		return ofy().query(clazz).ancestor(parent).listKeys();
	}

	public List<T> listChildren(Object parent) {
		return ofy().query(clazz).ancestor(parent).list();
	}

	public List<Key<T>> listKeysByProperty(String propName, Object propValue) {
		Query<T> q = ofy().query(clazz);
		q.filter(propName, propValue);
		return q.listKeys();
	}

	public Key<T> put(T entity)

	{
		return ofy().put(entity);
	}

	public Map<Key<T>, T> putAll(Iterable<T> entities) {
		return ofy().put(entities);
	}

}