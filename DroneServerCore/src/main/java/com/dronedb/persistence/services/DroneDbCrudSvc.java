package com.dronedb.persistence.services;

import java.util.Set;
import java.util.UUID;

import com.dronedb.persistence.exception.DatabaseValidationException;
import com.dronedb.persistence.scheme.BaseObject;

public interface DroneDbCrudSvc {
	
	String CheckConnection();
		
	<T extends BaseObject> T create(final Class<T> clz);
	
	<T extends BaseObject> T update(T object) throws DatabaseValidationException;
	
	<T extends BaseObject> void updateSet(Set<T> objects);
	
	<T extends BaseObject> void delete(T object);
	
	<T extends BaseObject> T read(final UUID objId);
	
	<T extends BaseObject> T readByClass(final UUID objId, final Class<T> clz);
}
