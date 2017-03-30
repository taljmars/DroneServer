package com.dronedb.persistence.services;

import java.util.List;
import com.dronedb.persistence.scheme.BaseObject;

public interface QuerySvc {
	
	<T extends BaseObject> List<? extends BaseObject> runNativeQuery(String query, Class<T> clz); 
	
	<T extends BaseObject> List<? extends BaseObject> runNamedQuery(String query, Class<T> clz);
	
	<T extends BaseObject> List<? extends BaseObject> query(QueryRequest queryRequest);
}
