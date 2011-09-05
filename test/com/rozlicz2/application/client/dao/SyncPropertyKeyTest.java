package com.rozlicz2.application.client.dao;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SyncPropertyKeyTest {
	
	private static final String SOME_KIND1 = "some kind1";
	private static final String SOME_KIND2 = "some kind2";
	private static final String SOME_PROPERTY_NAME1 = "some property1";
	private static final String SOME_PROPERTY_NAME2 = "some property2";
	private static final Object SOME_VALUE1 = "some value 1";
	private static final Object SOME_VALUE2 = "some value 2";

	@Before
	public void setUp() {
	}
	
	@Test
	public void testPropertyKey() {
		SyncPropertyKey syncPropertyKey1 = new SyncPropertyKey(SOME_KIND1);
		SyncPropertyKey syncPropertyKey2 = new SyncPropertyKey(SOME_KIND1);
		SyncPropertyKey syncPropertyKey4 = new SyncPropertyKey(SOME_KIND1, SOME_PROPERTY_NAME1);
		SyncPropertyKey syncPropertyKey5 = new SyncPropertyKey(SOME_KIND1, SOME_PROPERTY_NAME2);
		SyncPropertyKey syncPropertyKey6 = new SyncPropertyKey(SOME_KIND1, SOME_PROPERTY_NAME2, SOME_VALUE1);
		SyncPropertyKey syncPropertyKey7 = new SyncPropertyKey(SOME_KIND1, SOME_PROPERTY_NAME2, SOME_VALUE2);
		SyncPropertyKey syncPropertyKey3 = new SyncPropertyKey(SOME_KIND2);
		
		assertEquals(0, syncPropertyKey2.compareTo(syncPropertyKey1));
		assertEquals(0, syncPropertyKey1.compareTo(syncPropertyKey2));
		
		assertEquals(1, syncPropertyKey3.compareTo(syncPropertyKey1));
		assertEquals(-1, syncPropertyKey1.compareTo(syncPropertyKey3));

		assertEquals(1, syncPropertyKey4.compareTo(syncPropertyKey1));
		assertEquals(-1, syncPropertyKey1.compareTo(syncPropertyKey4));
		
		assertEquals(1, syncPropertyKey3.compareTo(syncPropertyKey4));
		assertEquals(-1, syncPropertyKey4.compareTo(syncPropertyKey3));
		
		assertEquals(1, syncPropertyKey5.compareTo(syncPropertyKey4));
		assertEquals(-1, syncPropertyKey4.compareTo(syncPropertyKey5));
		
		assertEquals(1, syncPropertyKey6.compareTo(syncPropertyKey5));
		assertEquals(-1, syncPropertyKey5.compareTo(syncPropertyKey6));
		
		assertEquals(1, syncPropertyKey3.compareTo(syncPropertyKey6));
		assertEquals(-1, syncPropertyKey6.compareTo(syncPropertyKey3));
		
		assertEquals(1, syncPropertyKey7.compareTo(syncPropertyKey6));
		assertEquals(-1, syncPropertyKey6.compareTo(syncPropertyKey7));
	}
}
