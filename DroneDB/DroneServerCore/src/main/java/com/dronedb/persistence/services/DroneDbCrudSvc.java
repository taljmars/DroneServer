package com.dronedb.persistence.services;

import java.util.Set;

import com.dronedb.persistence.scheme.BaseObject;

public interface DroneDbCrudSvc {
	
	String CheckConnection();
		
	<T extends BaseObject> T create(final Class<T> clz);
	
	<T extends BaseObject> T update(T object);
	
	<T extends BaseObject> void updateSet(Set<T> objects);
	
	<T extends BaseObject> void delete(T object);
	
	<T extends BaseObject> T read(final String objId);
	
	<T extends BaseObject> T readByClass(final String objId, final Class<T> clz);
	
}
