package com.rozlicz2.application.client.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SyncKeyTest {
	private static final int TEST_ID = 123;
	private static final String TEST_KIND = "test_kind";

	@Test
	public void testHashGeneration() {
		SyncKey key1 = new SyncKey(TEST_KIND, TEST_ID);
		SyncKey key2 = new SyncKey(TEST_KIND, TEST_ID);
		assertEquals(key1.hashCode(), key2.hashCode());
		assertEquals(0, key1.compareTo(key2));
		assertEquals(key1, key2);
	}
}
