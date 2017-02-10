package com.dronedb.ws.internal;

import javax.annotation.PostConstruct;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.dronedb.scheme.BaseObject;
import com.dronedb.services.DroneDbCrudSvc;
import com.dronedb.ws.DroneDbCrudSvcRemote;

@Component
@WebService( endpointInterface = "com.dronedb.ws.DroneDbCrudSvcRemote" )
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
	
	public <T extends BaseObject> T create(final Class<T> clz) {
		return droneDbCrudSvc.create(clz);
	}
	
	public <T extends BaseObject> void update(T object) {
		droneDbCrudSvc.update(object);
	}
	
	public <T extends BaseObject> void delete(T object) {
		droneDbCrudSvc.delete(object);
	}
	
	public <T extends BaseObject> T read(final Integer objId) {
		return droneDbCrudSvc.read(objId);
	}
	
	public <T extends BaseObject> T readByClass(final Integer objId, final Class<T> clz) {
		T object = droneDbCrudSvc.readByClass(objId, clz);
		System.out.println("Send object to client -> '" + object + "'");
		return object;
	}
}
