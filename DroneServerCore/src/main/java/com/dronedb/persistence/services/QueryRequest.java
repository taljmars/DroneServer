package com.dronedb.persistence.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

import com.dronedb.persistence.scheme.BaseObject;

public class QueryRequest {
	@XmlElement(required = true)	
	private String query;
	
	@XmlElement(required = true)	
	private Class<? extends BaseObject> clz;
		
	private Map<String, String> params;
	
	public QueryRequest() {
		params = new HashMap();
	}
	
	public void setClz(Class<? extends BaseObject> clz) {
		this.clz = clz;
	}
	
	public Class<? extends BaseObject> getClz() {
		return clz;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	public String getQuery() {
		return this.query;
	}
	
	public void addParameter(String param) {
		params.put(params.size() + "", param);
	}
	
	public Map<String, String> getParameters() {
		return params;
	}
	
	public void setParameters(Map<String, String> params) {
		this.params.putAll(params);
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [ Query=" + query + ", Clz=" + clz + ", Parameters=" + params.toString() + " ]";
	}
}
