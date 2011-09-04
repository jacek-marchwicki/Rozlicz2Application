package com.rozlicz2.application.client.dao;

import java.util.ArrayList;
import java.util.List;

public class SyncQuery implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static class Filter {

		private final String propertyName;
		private final FilterOperator operator;
		private final Object value;
		
		public String getPropertyName() {
			return propertyName;
		}

		public FilterOperator getOperator() {
			return operator;
		}

		public Object getValue() {
			return value;
		}

		public Filter(String propertyName, FilterOperator operator, Object value) {
			this.propertyName = propertyName;
			this.operator = operator;
			this.value = value;
		}
	}
	
	public static enum FilterOperator {
		EQUAL,
		GREATER_THAN
	}

	private final String kind;
	
	private List<Filter> filters = new ArrayList<SyncQuery.Filter>();
	
	public SyncQuery(String kind) {
		this.kind = kind;
	}
	
	public void addFilter(String propertyName, FilterOperator operator, Object value) {
		Filter filter = new Filter(propertyName, operator, value);
		filters.add(filter);
	}

	public String getKind() {
		return kind;
	}
}
