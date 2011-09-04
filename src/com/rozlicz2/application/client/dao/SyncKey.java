package com.rozlicz2.application.client.dao;

public class SyncKey implements java.io.Serializable, java.lang.Comparable<SyncKey> {
	private final String kind;
	private final long id;
	transient private Integer hash;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SyncKey(String kind, long id) {
		this.kind = kind;
		this.id = id;
	}

	@Override
	public int compareTo(SyncKey arg0) {
		int compare = kind.compareTo(arg0.kind);
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
		String hashString = kind + "|" + Long.toString(id);
		hash = new Integer(hashString.hashCode());
		return hash;
	}

	public String getKind() {
		return kind;
	}

	public long getId() {
		return id;
	}

}
