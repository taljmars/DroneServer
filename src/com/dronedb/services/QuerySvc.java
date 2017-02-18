package com.dronedb.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.postgresql.util.PGobject;

import com.dronedb.scheme.BaseObject;

public interface QuerySvc {
	
	<T extends BaseObject> List<? extends BaseObject> runNativeQuery(String query, Class<T> clz); 
	
	<T extends BaseObject> List<? extends BaseObject> runNamedQuery(String query, Class<T> clz);
}
