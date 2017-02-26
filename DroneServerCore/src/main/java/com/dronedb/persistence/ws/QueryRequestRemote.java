package com.dronedb.persistence.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.dronedb.persistence.scheme.BaseObject;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

@XmlRootElement
public class QueryRequestRemote {
	
	protected String query;
	
	private Class<? extends BaseObject> clz;
		
	private Map<String, String> params;
	
	public QueryRequestRemote() {
		params = new HashMap();
	}
	
	@Setter
	public void setClz(Class<? extends BaseObject> clz) {
		this.clz = clz;
	}
	
	@Getter
	@XmlElement(required = true)	
	public Class<? extends BaseObject> getClz() {
		return clz;
	}
	
	@Setter
	public void setQuery(String query) {
		this.query = query;
	}
	
	@Getter
	@XmlElement(required = true)
	public String getQuery() {
		return this.query;
	}
	
	public void addParameter(String param) {
		params.put(params.size() + "",param);
	}
	
	public void addParameter(String paramName, String param) {
		params.put(paramName, param);
	}
	
	@Getter
	@XmlElement(required = true)
	public Map<String, String> getParameters() {
		return params;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [ Query=" + query + ", Clz=" + clz + ", Parameters=" + params.toString() + " ]";
	}
}
