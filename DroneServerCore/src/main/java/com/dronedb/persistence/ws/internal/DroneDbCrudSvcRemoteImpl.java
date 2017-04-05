package com.dronedb.persistence.ws.internal;

import javax.annotation.PostConstruct;
import javax.jws.WebService;

import com.dronedb.persistence.exception.DatabaseValidationException;
import com.dronedb.persistence.scheme.DatabaseRemoteValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.dronedb.persistence.services.DroneDbCrudSvc;
import com.dronedb.persistence.scheme.DroneDbCrudSvcRemote;
import com.dronedb.persistence.scheme.BaseObject;

import java.util.UUID;

@Component
@WebService(endpointInterface = "com.dronedb.persistence.scheme.DroneDbCrudSvcRemote")
public class DroneDbCrudSvcRemoteImpl implements DroneDbCrudSvcRemote
{
	
	@Autowired
	private DroneDbCrudSvc droneDbCrudSvc;
	
	@PostConstruct
	public void init() {
		Assert.notNull(droneDbCrudSvc,"Failed to initiate 'droneDbCrudSvc'");
	}

	@Override
	public String CheckConnection() {
		return droneDbCrudSvc.CheckConnection();
	}
	
	@Override
	public <T extends BaseObject> T create(final Class<T> clz) {
		System.out.println("Crud REMOTE CREATE called " + clz);
		return (T) droneDbCrudSvc.create(clz).copy();
	}
	
	@Override
	public <T extends BaseObject> T update(T object) throws DatabaseRemoteValidationException {
		System.out.println("Crud REMOTE UPDATE called " + object);
		T obj = null;
		try {
			obj = droneDbCrudSvc.update(object);
			return (T) obj.copy();
		}
		catch (DatabaseValidationException e) {
			throw new DatabaseRemoteValidationException(e.getMessage());
		}
	}
	
//	@Override
//	public <T extends BaseObject> void updateSet(Set<T> objects) {
//		droneDbCrudSvc.updateSet(objects);
//	}
	
	@Override
	public <T extends BaseObject> void delete(T object) {
		droneDbCrudSvc.delete(object);
	}
	
	@Override
	public <T extends BaseObject> T read(final UUID objId) {
		System.out.println("Crud REMOTE READ called " + objId);
		return (T) droneDbCrudSvc.read(objId).copy();
	}
	
	@Override
	public <T extends BaseObject> T readByClass(final UUID objId, final Class<T> clz) {
		System.out.println("Crud REMOTE READ called " + objId + ", class " + clz);
		T object = droneDbCrudSvc.readByClass(objId, clz);
		System.out.println("Send object to client -> '" + object + "'");
		return (T) object.copy();
	}
}
