package com.dronedb.services;

import com.dronedb.scheme.BaseObject;

public interface DroneDbCrudSvc {
	
	String CheckConnection();
		
	<T extends BaseObject> T create(final Class<T> clz);
	
	<T extends BaseObject> void update(T object);
	
	<T extends BaseObject> void delete(T object);
	
	<T extends BaseObject> T read(final Integer objId);
	
	<T extends BaseObject> T readByClass(final Integer objId, final Class<T> clz);
	
}
