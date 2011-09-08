package com.rozlicz2.application.client.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.rozlicz2.application.client.dao.SyncQuery.FilterOperator;

public class IntegrationTest {

	private static final String ANIMAL_PARAMETER_NAME = "animal";
	private static final String CAT = "cat";
	private static final String DOG = "dog";
	private static final String EXAMPLE_FIRST_NAME1 = "Antonio";
	private static final String EXAMPLE_FIRST_NAME2 = "Frantionio";
	private static final String EXAMPLE_FIRST_NAME3 = "Franco";
	private static final String EXAMPLE_FIRST_NAME4 = "Marco";
	private static final String EXAMPLE_FIRST_NAME5 = "Jacko";
	private static final String EXAMPLE_FIRST_NAME6 = "Adamo";
	private static final String EXAMPLE_KEY = "test1";
	private static final String EXAMPLE_KEY_KIND = "Employee";
	private static final String EXAMPLE_SURNAME1 = "Gilberto";
	private static final String EXAMPLE_SURNAME2 = "Tatanto";
	private static final String FIRST_NAME_PARAMETER_NAME = "firstName";
	private static final String MOUSE = "mouse";
	private static final String SURNAME_PARAMETER_NAME = "surname";
	private SyncDatastoreService datastore;

	@After
	public void cleanUp() {
		datastore.clean();
	}

	private SyncEntity createEmployee(String name) {
		SyncEntity employee = new SyncEntity(EXAMPLE_KEY_KIND);
		employee.setProperty(FIRST_NAME_PARAMETER_NAME, name);
		return employee;
	}

	private SyncEntity createEmployee(String name, String surname) {
		SyncEntity employee = new SyncEntity(EXAMPLE_KEY_KIND);
		employee.setProperty(FIRST_NAME_PARAMETER_NAME, name);
		employee.setProperty(SURNAME_PARAMETER_NAME, surname);
		return employee;
	}

	private void fillDatabaseList() {
		{
			SyncEntity employee = new SyncEntity(EXAMPLE_KEY_KIND);
			employee.setProperty(FIRST_NAME_PARAMETER_NAME, EXAMPLE_FIRST_NAME1);
			List<String> animals = Arrays
					.asList(new String[] { DOG, CAT, MOUSE });
			employee.setProperty(ANIMAL_PARAMETER_NAME, animals);
			datastore.put(employee);
		}
		{
			SyncEntity employee = new SyncEntity(EXAMPLE_KEY_KIND);
			employee.setProperty(FIRST_NAME_PARAMETER_NAME, EXAMPLE_FIRST_NAME2);
			List<String> animals = Arrays.asList(new String[] {});
			employee.setProperty(ANIMAL_PARAMETER_NAME, animals);
			datastore.put(employee);
		}
		{
			SyncEntity employee = new SyncEntity(EXAMPLE_KEY_KIND);
			employee.setProperty(FIRST_NAME_PARAMETER_NAME, EXAMPLE_FIRST_NAME3);
			// Twice times addedding CAT
			List<String> animals = Arrays
					.asList(new String[] { DOG, CAT, CAT });
			employee.setProperty(ANIMAL_PARAMETER_NAME, animals);
			datastore.put(employee);
		}
	}

	private void fillDatabaseNames() {
		insertEmployee(EXAMPLE_FIRST_NAME1);
		insertEmployee(EXAMPLE_FIRST_NAME2);
		insertEmployee(EXAMPLE_FIRST_NAME3);
		insertEmployee(EXAMPLE_FIRST_NAME4);
		insertEmployee(EXAMPLE_FIRST_NAME5);
		insertEmployee(EXAMPLE_FIRST_NAME6);
	}

	private void fillDatabaseNamesSurnames() {
		insertEmployee(EXAMPLE_FIRST_NAME1, EXAMPLE_SURNAME1);
		insertEmployee(EXAMPLE_FIRST_NAME2, EXAMPLE_SURNAME1);
		insertEmployee(EXAMPLE_FIRST_NAME3, EXAMPLE_SURNAME1);
		insertEmployee(EXAMPLE_FIRST_NAME4, EXAMPLE_SURNAME1);
		insertEmployee(EXAMPLE_FIRST_NAME5, EXAMPLE_SURNAME2);
		insertEmployee(EXAMPLE_FIRST_NAME6, EXAMPLE_SURNAME2);
	}

	private SyncEntity insertEmployee(String name) {
		SyncEntity employee = createEmployee(name);
		datastore.put(employee);
		return employee;
	}

	private SyncEntity insertEmployee(String name, String surname) {
		SyncEntity employee = createEmployee(name, surname);
		datastore.put(employee);
		return employee;
	}

	private SyncEntity insertEmployee1() {
		return insertEmployee(EXAMPLE_FIRST_NAME1);
	}

	private SyncEntity insertEmployee2() {
		return insertEmployee(EXAMPLE_FIRST_NAME2);
	}

	@Before
	public void setUp() {
		datastore = SyncDatastoreServiceFactory.getDatastoreService();
	}

	@Test
	public void testClean() {
		SyncEntity employee = insertEmployee1();
		datastore.clean();
		SyncKey k = employee.getKey();
		datastore.clean();
		SyncEntity entity = datastore.get(k);
		assertNull(entity);
	}

	@Test
	public void testEntity() {
		SyncEntity employee = createEmployee(EXAMPLE_FIRST_NAME1);
		Object property = employee.getProperty(FIRST_NAME_PARAMETER_NAME);
		assertEquals(EXAMPLE_FIRST_NAME1, property);
	}

	@Test
	public void testGet() {
		SyncEntity employee = insertEmployee1();
		SyncKey k = employee.getKey();
		SyncEntity entity = datastore.get(k);
		String property = (String) entity
				.getProperty(FIRST_NAME_PARAMETER_NAME);
		assertEquals(EXAMPLE_FIRST_NAME1, property);

	}

	@Test
	public void testGreaterQueries() {
		fillDatabaseNames();
		SyncQuery q;
		SyncPreparedQuery pq;
		q = new SyncQuery(EXAMPLE_KEY_KIND);
		q.addFilter(FIRST_NAME_PARAMETER_NAME, FilterOperator.EQUAL,
				EXAMPLE_FIRST_NAME1);
		pq = datastore.prepare(q);
		testIsIn(pq, EXAMPLE_FIRST_NAME1);
	}

	@Test
	public void testGreaterQueries2() {
		fillDatabaseNames();
		SyncQuery q = new SyncQuery(EXAMPLE_KEY_KIND);
		q.addFilter(FIRST_NAME_PARAMETER_NAME, FilterOperator.GREATER_THAN,
				EXAMPLE_FIRST_NAME5);
		SyncPreparedQuery pq = datastore.prepare(q);
		testIsIn(pq, EXAMPLE_FIRST_NAME6);
	}

	@Test
	public void testInsertDeleteWithMultiplication() {
		fillDatabaseList();
		SyncQuery q = new SyncQuery(EXAMPLE_KEY_KIND);
		q.addFilter(ANIMAL_PARAMETER_NAME, FilterOperator.EQUAL, CAT);
		SyncPreparedQuery pq = datastore.prepare(q);
		testIsIn(pq, EXAMPLE_FIRST_NAME1, EXAMPLE_FIRST_NAME3);

		q = new SyncQuery(EXAMPLE_KEY_KIND);
		q.addFilter(FIRST_NAME_PARAMETER_NAME, FilterOperator.EQUAL,
				EXAMPLE_FIRST_NAME3);

		pq = datastore.prepare(q);
		SyncEntity entity = pq.asSingleEntity();
		assertNotNull(entity);

		datastore.delete(entity.getKey());

		q = new SyncQuery(EXAMPLE_KEY_KIND);
		q.addFilter(ANIMAL_PARAMETER_NAME, FilterOperator.EQUAL, CAT);
		pq = datastore.prepare(q);
		testIsIn(pq, EXAMPLE_FIRST_NAME1);
	}

	@Test
	public void testInsertList() {
		fillDatabaseList();
		SyncQuery q = new SyncQuery(EXAMPLE_KEY_KIND);
		q.addFilter(FIRST_NAME_PARAMETER_NAME, FilterOperator.EQUAL,
				EXAMPLE_FIRST_NAME1);
		SyncPreparedQuery pq = datastore.prepare(q);
		testIsIn(pq, EXAMPLE_FIRST_NAME1);
	}

	private void testIsIn(SyncPreparedQuery pq, String... names) {
		Map<String, String> namesHash = new HashMap<String, String>();
		int expectedCount = 0;
		for (String name : names) {
			namesHash.put(name, name);
			expectedCount++;
		}
		int count = 0;
		for (SyncEntity result : pq.asIterable()) {
			String name = (String) result
					.getProperty(FIRST_NAME_PARAMETER_NAME);
			boolean containsKey = namesHash.containsKey(name);
			assertTrue(containsKey);
			count++;
		}
		assertEquals(expectedCount, count);
	}

	@Test
	public void testKey() {
		SyncKey k = SyncKeyFactory.createKey(EXAMPLE_KEY_KIND, EXAMPLE_KEY);
		SyncEntity entity = datastore.get(k);
		assertNull(entity);

		SyncEntity employee = new SyncEntity(k);
		employee.setProperty(FIRST_NAME_PARAMETER_NAME, EXAMPLE_FIRST_NAME1);
		datastore.put(employee);

		entity = datastore.get(k);
		String property = (String) entity
				.getProperty(FIRST_NAME_PARAMETER_NAME);
		assertEquals(EXAMPLE_FIRST_NAME1, property);
	}

	@Test
	public void testMultipleParameters1() {
		fillDatabaseNamesSurnames();
		SyncQuery q = new SyncQuery(EXAMPLE_KEY_KIND);
		q.addFilter(FIRST_NAME_PARAMETER_NAME, FilterOperator.EQUAL,
				EXAMPLE_FIRST_NAME1);
		q.addFilter(SURNAME_PARAMETER_NAME, FilterOperator.EQUAL,
				EXAMPLE_SURNAME1);
		SyncPreparedQuery pq = datastore.prepare(q);
		testIsIn(pq, EXAMPLE_FIRST_NAME1);
	}

	@Test
	public void testMultipleParameters2() {
		fillDatabaseNamesSurnames();
		SyncQuery q = new SyncQuery(EXAMPLE_KEY_KIND);
		q.addFilter(SURNAME_PARAMETER_NAME, FilterOperator.EQUAL,
				EXAMPLE_SURNAME1);
		SyncPreparedQuery pq = datastore.prepare(q);
		testIsIn(pq, EXAMPLE_FIRST_NAME1, EXAMPLE_FIRST_NAME2,
				EXAMPLE_FIRST_NAME3, EXAMPLE_FIRST_NAME4);
	}

	@Test
	public void testPut() {
		SyncEntity employee = createEmployee(EXAMPLE_FIRST_NAME1);
		datastore.put(employee);
	}

	@Test
	public void testQuery() {
		insertEmployee1();
		insertEmployee2();
		SyncQuery q = new SyncQuery(EXAMPLE_KEY_KIND);
		q.addFilter(FIRST_NAME_PARAMETER_NAME, FilterOperator.EQUAL,
				EXAMPLE_FIRST_NAME1);
		SyncPreparedQuery pq = datastore.prepare(q);
		SyncEntity result = pq.asSingleEntity();
		assertNotNull(result);
		String property = (String) result
				.getProperty(FIRST_NAME_PARAMETER_NAME);
		assertEquals(EXAMPLE_FIRST_NAME1, property);

		q = new SyncQuery(EXAMPLE_KEY_KIND);
		q.addFilter(FIRST_NAME_PARAMETER_NAME, FilterOperator.EQUAL,
				EXAMPLE_FIRST_NAME2);
		pq = datastore.prepare(q);
		result = pq.asSingleEntity();
		assertNotNull(result);
		property = (String) result.getProperty(FIRST_NAME_PARAMETER_NAME);
		assertEquals(EXAMPLE_FIRST_NAME2, property);
	}

	@Test
	public void testQueryByList() {
		fillDatabaseList();
		SyncQuery q = new SyncQuery(EXAMPLE_KEY_KIND);
		q.addFilter(ANIMAL_PARAMETER_NAME, FilterOperator.EQUAL, DOG);
		SyncPreparedQuery pq = datastore.prepare(q);
		testIsIn(pq, EXAMPLE_FIRST_NAME1, EXAMPLE_FIRST_NAME3);
	}

	@Test
	public void testQueryIterator1() {
		insertEmployee1();
		insertEmployee2();

		SyncQuery q = new SyncQuery(EXAMPLE_KEY_KIND);
		SyncPreparedQuery pq = datastore.prepare(q);
		int counter = 0;
		for (@SuppressWarnings("unused")
		SyncEntity result : pq.asIterable()) {
			counter++;
		}
		assertEquals(2, counter);
	}

	@Test
	public void testQueryIterator2() {
		insertEmployee1();
		insertEmployee2();
		SyncQuery q = new SyncQuery(EXAMPLE_KEY_KIND);
		q.addFilter(FIRST_NAME_PARAMETER_NAME, FilterOperator.EQUAL,
				EXAMPLE_FIRST_NAME1);
		SyncPreparedQuery pq = datastore.prepare(q);
		int counter = 0;
		for (SyncEntity result : pq.asIterable()) {
			String property = (String) result
					.getProperty(FIRST_NAME_PARAMETER_NAME);
			assertEquals(EXAMPLE_FIRST_NAME1, property);
			counter++;
		}
		assertEquals(1, counter);
	}

	@Test
	public void testQueryIterator3() {
		insertEmployee1();
		insertEmployee2();
		SyncQuery q = new SyncQuery(EXAMPLE_KEY_KIND);
		q.addFilter(FIRST_NAME_PARAMETER_NAME, FilterOperator.EQUAL,
				EXAMPLE_FIRST_NAME2);
		SyncPreparedQuery pq = datastore.prepare(q);
		int counter = 0;
		for (SyncEntity result : pq.asIterable()) {
			String property = (String) result
					.getProperty(FIRST_NAME_PARAMETER_NAME);
			assertEquals(EXAMPLE_FIRST_NAME2, property);
			counter++;
		}
		assertEquals(1, counter);
	}
}
