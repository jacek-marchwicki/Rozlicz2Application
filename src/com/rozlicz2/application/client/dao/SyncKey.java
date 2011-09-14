package com.rozlicz2.application.client.dao;

public class SyncKey implements java.io.Serializable,
		java.lang.Comparable<SyncKey> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static SyncKey fromString(String token) {
		int indexOf = token.indexOf('|');
		assert (token.length() > indexOf);
		String kind = token.substring(0, indexOf);
		String idStr = token.substring(indexOf + 1);
		long id = Long.parseLong(idStr);

		return new SyncKey(kind, id);
	}

	transient private Integer hash;
	private final long id;

	private final String kind;

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
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof SyncKey)) {
			return false;
		}
		SyncKey k = (SyncKey) o;
		if (!kind.equals(k.kind))
			return false;
		if (id != k.id)
			return false;
		return true;
	}

	public long getId() {
		return id;
	}

	public String getKind() {
		return kind;
	}

	@Override
	public int hashCode() {
		if (hash != null)
			return hash.intValue();
		String hashString = kind + "|" + Long.toString(id);
		hash = new Integer(hashString.hashCode());
		return hash.intValue();
	}

	@Override
	public String toString() {
		return kind + "|" + Long.toString(id);
	}

}
