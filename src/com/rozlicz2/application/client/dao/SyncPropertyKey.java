package com.rozlicz2.application.client.dao;

public class SyncPropertyKey implements java.io.Serializable,
		java.lang.Comparable<SyncPropertyKey>, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static String objectToString(Object value) {
		if (value == null)
			return "";

		if (value instanceof String) {
			return (String) value;
		}
		if (value instanceof Integer) {
			Integer integerValue = (Integer) value;
			int intValue = integerValue.intValue();
			return Integer.toString(intValue);
		}
		if (value instanceof Long) {
			Long longegerValue = (Long) value;
			long longValue = longegerValue.longValue();
			return Long.toString(longValue);
		}
		return value.toString();
	}

	transient private Integer hash;
	private final Long id;
	private final String kind;
	private final String propertyName;

	private final String value;

	public SyncPropertyKey(String kind) {
		this.kind = kind;
		this.propertyName = null;
		this.value = null;
		this.id = null;
	}

	public SyncPropertyKey(String kind, String propertyName) {
		this.kind = kind;
		this.propertyName = propertyName;
		this.value = null;
		this.id = null;
	}

	public SyncPropertyKey(String kind, String propertyName, Object value) {
		this.kind = kind;
		this.propertyName = propertyName;
		this.value = objectToString(value);
		this.id = null;
	}

	public SyncPropertyKey(String kind, String propertyName, Object value,
			long id) {
		this.kind = kind;
		this.propertyName = propertyName;
		this.value = objectToString(value);
		this.id = id;
	}

	public SyncPropertyKey(SyncKey key, String propertyName, Object value) {
		this.kind = key.getKind();
		this.propertyName = propertyName;
		this.value = objectToString(value);
		this.id = key.getId();
	}

	public SyncPropertyKey cloneMe() {
		return new SyncPropertyKey(kind, propertyName, value, id);
	}

	private int compareLongWithNulls(Long arg1, Long arg2) {
		if (arg1 == null) {
			if (arg2 == null)
				return 0;
			return -1;
		}
		if (arg2 == null)
			return 1;
		return arg1.compareTo(arg2);
	}

	private int compareStringWithNulls(String arg1, String arg2) {
		if (arg1 == null) {
			if (arg2 == null)
				return 0;
			return -1;
		}
		if (arg2 == null)
			return 1;
		return arg1.compareTo(arg2);
	}

	@Override
	public int compareTo(SyncPropertyKey arg0) {
		int compare = kind.compareTo(arg0.kind);
		if (compare != 0)
			return compare;
		compare = compareStringWithNulls(propertyName, arg0.propertyName);
		if (compare != 0)
			return compare;
		compare = compareStringWithNulls(value, arg0.value);
		if (compare != 0)
			return compare;
		return compareLongWithNulls(id, arg0.id);
	}

	public int contains(SyncPropertyKey arg0) {
		int compare = kind.compareTo(arg0.kind);
		if (compare != 0)
			return compare;
		compare = containsStringWithNulls(propertyName, arg0.propertyName);
		if (compare != 0)
			return compare;
		compare = containsStringWithNulls(value, arg0.value);
		if (compare != 0)
			return compare;
		return containsLongWithNulls(id, arg0.id);
	}

	/**
	 * Chech whetherver arg1 contain arg2
	 * 
	 * @param arg1
	 * @param arg2
	 * @return -1, 0, 1 - arg1 is smaller then arg2, arg1 contain arg2, arg1 is
	 *         greater than arg2
	 */
	private int containsLongWithNulls(Long arg1, Long arg2) {
		if (arg1 == null) {
			return 0;
		}
		if (arg2 == null)
			return 1;
		return arg1.compareTo(arg2);
	}

	/**
	 * Chech whetherver arg1 contain arg2
	 * 
	 * @param arg1
	 * @param arg2
	 * @return -1, 0, 1 - arg1 is smaller then arg2, arg1 contain arg2, arg1 is
	 *         greater than arg2
	 */
	private int containsStringWithNulls(String arg1, String arg2) {
		if (arg1 == null) {
			return 0;
		}
		if (arg2 == null)
			return 1;
		return arg1.compareTo(arg2);
	}

	@Override
	public int hashCode() {
		if (hash != null)
			return hash.intValue();
		String hashString = this.toString();
		hash = new Integer(hashString.hashCode());
		return hash;
	}

	@Override
	public String toString() {
		if (id != null) {
			return kind + "|" + propertyName + "|" + value + "|"
					+ Long.toString(id) + "|";
		}
		if (value != null) {
			return kind + "|" + propertyName + "|" + value + "|";
		}
		if (propertyName != null) {
			return kind + "|" + propertyName + "|";
		}
		return kind + "|";
	}

}
