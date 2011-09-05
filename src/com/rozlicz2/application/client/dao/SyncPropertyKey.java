package com.rozlicz2.application.client.dao;

public class SyncPropertyKey implements java.io.Serializable, java.lang.Comparable<SyncPropertyKey>, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String kind;
	private final String propertyName;
	private final String value;
	private final Long id;
	transient private Integer hash;
	
	public SyncPropertyKey(String kind, String propertyName, Object value, long id) {
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
			return kind + "|" + propertyName + "|" + value +"|" + Long.toString(id) + "|";
		}
		if (value != null) {
			return kind + "|" + propertyName + "|" + value +"|";
		}
		if (propertyName != null) {
			return kind + "|" + propertyName + "|";
		}
		return kind + "|";
	}
	
	@Override
	public SyncPropertyKey clone() {
		return new SyncPropertyKey(kind, propertyName, value, id);
	}

}
