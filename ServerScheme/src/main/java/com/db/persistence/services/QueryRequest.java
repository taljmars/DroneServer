package com.db.persistence.services;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

import com.db.persistence.scheme.BaseObject;

public class QueryRequest {

	@XmlElement(required = true)	
	private String query;
	
	@XmlElement(required = true)	
	private Class<? extends BaseObject> clz;
		
	private Map<String, Object> params;

	private int limit;

	private int offset;

	// Ctor
	public QueryRequest() {
		params = new HashMap<>();
	}

	/**
	 * Set the expected type of the object returns from the query
	 * The request parameter must inherit the BaseObject object
	 * @param clz Clas type to be searched
	 */
	public void setClz(Class<? extends BaseObject> clz) {
		this.clz = clz;
	}

	/**
	 * Get the class type expected by the query
	 * @return Class type extends BaseObject
	 */
	public Class<? extends BaseObject> getClz() {
		return clz;
	}

	/**
	 * Set the query name to be executed, the request paramter should be a named query parameter
	 * @param query Named query exist in the database
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * Get the named query to be executed later.
	 * @return Named query string value
	 */
	public String getQuery() {
		return this.query;
	}

	/**
	 * Get the parameters list to be supplied to the query during it execution.
	 * @return Map representing the parameter name (of the query) and it value
	 */
	public Map<String, Object> getParameters() {
		return params;
	}

	/**
	 * Set the parameters list to be supplied to the user, every parameter in a query have a unique name,
	 * The supplied map hold the parameter name as a key and the value query as it value.
	 * @param params Map contain param name and it value
	 */
	public void setParameters(Map<String, String> params) {
		this.params.putAll(params);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [ Query=" + query + ", Clz=" + clz.getCanonicalName() + ", Parameters=" + params.toString() + " ]";
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
}
