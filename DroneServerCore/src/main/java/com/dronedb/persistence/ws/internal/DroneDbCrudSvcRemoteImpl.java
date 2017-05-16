package com.dronedb.persistence.ws.internal;

import javax.annotation.PostConstruct;
import javax.jws.WebService;

import com.dronedb.persistence.exception.DatabaseValidationException;
import com.dronedb.persistence.exception.ObjectInstanceException;
import com.dronedb.persistence.scheme.DatabaseValidationRemoteException;
import com.dronedb.persistence.scheme.ObjectInstanceRemoteException;
import javassist.tools.rmi.ObjectNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.dronedb.persistence.services.DroneDbCrudSvc;
import com.dronedb.persistence.scheme.DroneDbCrudSvcRemote;
import com.dronedb.persistence.scheme.BaseObject;

import java.util.List;
import java.util.UUID;

@Component
@WebService(endpointInterface = "com.dronedb.persistence.scheme.DroneDbCrudSvcRemote")
public class DroneDbCrudSvcRemoteImpl implements DroneDbCrudSvcRemote
{
	final static Logger logger = Logger.getLogger(DroneDbCrudSvcRemoteImpl.class);
	
	@Autowired
	private DroneDbCrudSvc droneDbCrudSvc;
	
	@PostConstruct
	public void init() {
		Assert.notNull(droneDbCrudSvc,"Failed to initiate 'droneDbCrudSvc'");
	}

	@Override
	public <T extends BaseObject> T create(final Class<T> clz) throws ObjectInstanceRemoteException {
		logger.debug("Crud REMOTE CREATE called " + clz);
		try {
			return (T) droneDbCrudSvc.create(clz).copy();
		}
		catch (ObjectInstanceException e) {
			logger.error("Failed to create object", e);
			throw new ObjectInstanceRemoteException("Failed to create object");
		}
	}
	
	@Override
	public <T extends BaseObject> T update(T object) throws DatabaseValidationRemoteException, ObjectInstanceRemoteException {
		logger.debug("Crud REMOTE UPDATE called " + object);
		T obj = null;
		try {
			obj = droneDbCrudSvc.update(object);
			return (T) obj.copy();
		}
		catch (DatabaseValidationException e) {
			logger.error("Failed to update object", e);
			throw new DatabaseValidationRemoteException("Failed to update object");
		}
		catch (ObjectInstanceException e) {
			logger.error("Failed to update object", e);
			throw new ObjectInstanceRemoteException("Failed to update object");
		}
	}
	
	@Override
	public <T extends BaseObject> void updateSet(List<T> objects) throws DatabaseValidationRemoteException, ObjectInstanceRemoteException {
		try {
			droneDbCrudSvc.updateSet(objects);
		}
		catch (DatabaseValidationException e) {
			logger.error("Failed to update object", e);
			throw new DatabaseValidationRemoteException("Failed to update object");
		}
		catch (ObjectInstanceException e) {
			logger.error("Failed to update object", e);
			throw new ObjectInstanceRemoteException("Failed to update object");
		}
	}
	
	@Override
	public <T extends BaseObject> void delete(T object) throws ObjectInstanceRemoteException, DatabaseValidationRemoteException {
		try {
			droneDbCrudSvc.delete(object);
		}
		catch (DatabaseValidationException e) {
			logger.error("Failed to delete object", e);
			throw new DatabaseValidationRemoteException("Failed to delete object");
		}
		catch (ObjectInstanceException | ObjectNotFoundException e) {
			logger.error("Failed to delete object", e);
			throw new ObjectInstanceRemoteException("Failed to delete object");
		}
	}
	
	@Override
	public BaseObject read(final UUID objId)  throws ObjectNotFoundException{
		logger.debug("Crud REMOTE READ called " + objId);
		BaseObject object = droneDbCrudSvc.read(objId);
		if (object == null) {
			logger.error("Failed to find object '" + objId + "'");
			throw new ObjectNotFoundException("Failed to find object '" + objId + "'");
		}
		return object.copy();
	}
	
	@Override
	public <T extends BaseObject> T readByClass(final UUID objId, final Class<T> clz) throws ObjectNotFoundException {
		logger.debug("Crud REMOTE READ called " + objId + ", class " + clz);
		T object = droneDbCrudSvc.readByClass(objId, clz);
		if (object == null) {
			logger.error("Failed to find object '" + objId + "'");
			throw new ObjectNotFoundException("Failed to find object '" + objId + "'");
		}
		return (T) object.copy();
	}
}
