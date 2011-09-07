package com.rozlicz2.application.client.dao;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.rozlicz2.application.client.dao.SyncQuery.FilterOperator;

public class IntegrationTest {

	private static final String EXAMPLE_KEY_KIND = "Employee";
	private static final String EXAMPLE_FIRST_NAME = "Antonio";
	private static final String EXAMPLE_FIRST_NAME2 = "Frantionio";
	private static final String FIRST_NAME_PARAMETER_NAME = "firstName";
	private static final String EXAMPLE_KEY = "test1";
	private SyncDatastoreService datastore;

	@Before
	public void setUp() {
		datastore = SyncDatastoreServiceFactory.getDatastoreService();
	}

	@After
	public void cleanUp() {
		datastore.clean();
	}

	@Test
	public void testEntity() {
		SyncEntity employee = createEmployee();
		Object property = employee.getProperty(FIRST_NAME_PARAMETER_NAME);
		assertEquals(EXAMPLE_FIRST_NAME, property);
	}

	@Test
	public void testPut() {
		SyncEntity employee = createEmployee();
		datastore.put(employee);
	}

	private SyncEntity createEmployee() {
		SyncEntity employee = new SyncEntity(EXAMPLE_KEY_KIND);
		employee.setProperty(FIRST_NAME_PARAMETER_NAME, EXAMPLE_FIRST_NAME);
		return employee;
	}

	private SyncEntity createEmployee2() {
		SyncEntity employee = new SyncEntity(EXAMPLE_KEY_KIND);
		employee.setProperty(FIRST_NAME_PARAMETER_NAME, EXAMPLE_FIRST_NAME2);
		return employee;
	}

	@Test
	public void testGet() {
		SyncEntity employee = insertEmployee();
		SyncKey k = employee.getKey();
		SyncEntity entity = datastore.get(k);
		String property = (String) entity
				.getProperty(FIRST_NAME_PARAMETER_NAME);
		assertEquals(EXAMPLE_FIRST_NAME, property);

	}

	@Test
	public void testClean() {
		SyncEntity employee = insertEmployee();
		datastore.clean();
		SyncKey k = employee.getKey();
		datastore.clean();
		SyncEntity entity = datastore.get(k);
		assertNull(entity);
	}

	private SyncEntity insertEmployee() {
		SyncEntity employee = createEmployee();
		datastore.put(employee);
		return employee;
	}

	private SyncEntity insertEmployee2() {
		SyncEntity employee = createEmployee2();
		datastore.put(employee);
		return employee;
	}

	@Test
	public void testKey() {
		SyncKey k = SyncKeyFactory.createKey(EXAMPLE_KEY_KIND, EXAMPLE_KEY);
		SyncEntity entity = datastore.get(k);
		assertNull(entity);

		SyncEntity employee = new SyncEntity(k);
		employee.setProperty(FIRST_NAME_PARAMETER_NAME, EXAMPLE_FIRST_NAME);
		datastore.put(employee);

		entity = datastore.get(k);
		String property = (String) entity
				.getProperty(FIRST_NAME_PARAMETER_NAME);
		assertEquals(EXAMPLE_FIRST_NAME, property);
	}

	@Test
	public void testQuery() {
		insertEmployee();
		insertEmployee2();
		SyncQuery q = new SyncQuery(EXAMPLE_KEY_KIND);
		q.addFilter(FIRST_NAME_PARAMETER_NAME, FilterOperator.EQUAL,
				EXAMPLE_FIRST_NAME);
		SyncPreparedQuery pq = datastore.perepare(q);
		SyncEntity result = pq.asSingleEntity();
		assertNotNull(result);
		String property = (String) result
				.getProperty(FIRST_NAME_PARAMETER_NAME);
		assertEquals(EXAMPLE_FIRST_NAME, property);

		q = new SyncQuery(EXAMPLE_KEY_KIND);
		q.addFilter(FIRST_NAME_PARAMETER_NAME, FilterOperator.EQUAL,
				EXAMPLE_FIRST_NAME2);
		pq = datastore.perepare(q);
		result = pq.asSingleEntity();
		assertNotNull(result);
		property = (String) result.getProperty(FIRST_NAME_PARAMETER_NAME);
		assertEquals(EXAMPLE_FIRST_NAME2, property);
	}

	@Test
	public void testQueryIterator() {
		insertEmployee();
		insertEmployee2();

		SyncQuery q = new SyncQuery(EXAMPLE_KEY_KIND);
		SyncPreparedQuery pq = datastore.perepare(q);
		int counter = 0;
		for (@SuppressWarnings("unused")
		SyncEntity result : pq.asIterable()) {
			counter++;
		}
		assertEquals(2, counter);

		q = new SyncQuery(EXAMPLE_KEY_KIND);
		q.addFilter(FIRST_NAME_PARAMETER_NAME, FilterOperator.EQUAL,
				EXAMPLE_FIRST_NAME);
		pq = datastore.perepare(q);
		counter = 0;
		for (SyncEntity result : pq.asIterable()) {
			String property = (String) result
					.getProperty(FIRST_NAME_PARAMETER_NAME);
			assertEquals(EXAMPLE_FIRST_NAME, property);
			counter++;
		}
		assertEquals(1, counter);

		q = new SyncQuery(EXAMPLE_KEY_KIND);
		q.addFilter(FIRST_NAME_PARAMETER_NAME, FilterOperator.EQUAL,
				EXAMPLE_FIRST_NAME2);
		pq = datastore.perepare(q);
		counter = 0;
		for (SyncEntity result : pq.asIterable()) {
			String property = (String) result
					.getProperty(FIRST_NAME_PARAMETER_NAME);
			assertEquals(EXAMPLE_FIRST_NAME2, property);
			counter++;
		}
		assertEquals(1, counter);
	}
}
