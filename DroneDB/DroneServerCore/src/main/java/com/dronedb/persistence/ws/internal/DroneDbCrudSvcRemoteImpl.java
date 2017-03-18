package com.dronedb.persistence.ws.internal;

import javax.annotation.PostConstruct;
import javax.jws.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.dronedb.persistence.services.DroneDbCrudSvc;
import com.dronedb.persistence.ws.DroneDbCrudSvcRemote;
import com.dronedb.persistence.scheme.BaseObject;

@Component
@WebService(endpointInterface = "com.dronedb.persistence.ws.DroneDbCrudSvcRemote")
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
		System.out.println("TALA In romte");
		return droneDbCrudSvc.CheckConnection();
	}
	
	@Override
	public <T extends BaseObject> T create(final Class<T> clz) {
		return droneDbCrudSvc.create(clz);
	}
	
	@Override
	public <T extends BaseObject> T update(T object) {
		return droneDbCrudSvc.update(object);
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
	public <T extends BaseObject> T read(final String objId) {
		return droneDbCrudSvc.read(objId);
	}
	
	@Override
	public <T extends BaseObject> T readByClass(final String objId, final Class<T> clz) {
		T object = droneDbCrudSvc.readByClass(objId, clz);
		System.out.println("Send object to client -> '" + object + "'");
		return object;
	}
}
