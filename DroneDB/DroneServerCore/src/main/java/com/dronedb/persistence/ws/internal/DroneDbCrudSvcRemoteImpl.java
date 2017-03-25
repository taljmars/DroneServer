package com.dronedb.persistence.ws.internal;

import javax.annotation.PostConstruct;
import javax.jws.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.dronedb.persistence.services.DroneDbCrudSvc;
import com.dronedb.persistence.scheme.apis.DroneDbCrudSvcRemote;
import com.dronedb.persistence.scheme.BaseObject;

import java.util.UUID;

@Component
@WebService(endpointInterface = "com.dronedb.persistence.scheme.apis.DroneDbCrudSvcRemote")
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
		return droneDbCrudSvc.create(clz);
	}
	
	@Override
	public <T extends BaseObject> T update(T object) {
		System.out.println("Crud REMOTE UPDATE called " + object);
		return (T) droneDbCrudSvc.update(object).copy();
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
		return droneDbCrudSvc.read(objId);
	}
	
	@Override
	public <T extends BaseObject> T readByClass(final UUID objId, final Class<T> clz) {
		System.out.println("Crud REMOTE READ called " + objId + ", class " + clz);
		T object = droneDbCrudSvc.readByClass(objId, clz);
		System.out.println("Send object to client -> '" + object + "'");
		return object;
	}
}
