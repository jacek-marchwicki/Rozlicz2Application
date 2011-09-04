package com.rozlicz2.application.client.dao;

public class SyncPropertyKey implements java.io.Serializable, java.lang.Comparable<SyncPropertyKey>, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String kind;
	private final String propertyName;
	private final String value;
	private final long id;
	transient private Integer hash;
	
	public SyncPropertyKey(String kind, long id, String propertyName, Object value) {
		this.kind = kind;
		this.propertyName = propertyName;
		this.id = id;
		this.value = objectToString(value);
	}
	
	public SyncPropertyKey(SyncKey key, String propertyName, Object value) {
		kind = key.getKind();
		id = key.getId();
		this.propertyName = propertyName;
		this.value = objectToString(value);
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

	@Override
	public int compareTo(SyncPropertyKey arg0) {
		int compare = kind.compareTo(arg0.kind);
		if (compare != 0)
			return compare;
		compare = propertyName.compareTo(arg0.propertyName);
		if (compare != 0)
			return compare;
		compare = propertyName.compareTo(arg0.value);
		if (compare != 0)
			return compare;
		if (id < arg0.id)
			return -1;
		if (id > arg0.id)
			return 1;
		return 0;
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
		return kind + "|" + propertyName + "|" + value +"|" + Long.toString(id);
	}
	
	@Override
	public SyncPropertyKey clone() {
		return new SyncPropertyKey(kind, id, propertyName, value);
	}

}
