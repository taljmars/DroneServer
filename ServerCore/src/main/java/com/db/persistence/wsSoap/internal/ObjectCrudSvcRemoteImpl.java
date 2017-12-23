package com.db.persistence.wsSoap.internal;

import com.db.persistence.exception.DatabaseValidationException;
import com.db.persistence.exception.ObjectInstanceException;
import com.db.persistence.remote_exception.DatabaseValidationRemoteException;
import com.db.persistence.remote_exception.ObjectInstanceRemoteException;
import com.db.persistence.remote_exception.ObjectNotFoundRemoteException;
import com.db.persistence.scheme.BaseObject;
import com.db.persistence.services.ObjectCrudSvc;
import com.db.persistence.wsSoap.ObjectCrudSvcRemote;
import javassist.tools.rmi.ObjectNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.jws.WebService;

@Component
@WebService(endpointInterface = "com.db.persistence.wsSoap.ObjectCrudSvcRemote"
		,targetNamespace = "scheme.persistence.db.com")
public class ObjectCrudSvcRemoteImpl implements ObjectCrudSvcRemote
{
	private final static Logger LOGGER = Logger.getLogger(ObjectCrudSvcRemoteImpl.class);

	@Autowired
	private ObjectCrudSvc objectCrudSvc;
	
	@PostConstruct
	public void init() {
		Assert.notNull(objectCrudSvc,"Failed to initiate 'objectCrudSvc'");
	}

	@Override
//	@WebResult(partName = "tal1", name = "tal2" ,targetNamespace = "http://scheme.persistence.dronedb.com/")
//	public <T extends BaseObject> T create(Class<T> clz) throws ObjectInstanceRemoteException {
	public <T extends BaseObject> T create(String clz) throws ObjectInstanceRemoteException {
		LOGGER.debug("Crud REMOTE CREATE called '" + clz + "'");
		try {
			T t = (T) objectCrudSvc.create(clz).copy();
			LOGGER.debug("TALMA Crud REMOTE CREATE called " + t);
			return t;
			//return (T) objectCrudSvc.create(clz).copy();
		}
		catch (ObjectInstanceException e) {
			LOGGER.error("Failed to create object", e);
			throw new ObjectInstanceRemoteException("Failed to create object");
		}
	}
	
	@Override
	public <T extends BaseObject> T update(T object) throws DatabaseValidationRemoteException, ObjectInstanceRemoteException {
		LOGGER.debug("Crud REMOTE UPDATE called " + object);
		try {
			T obj = objectCrudSvc.update(object);
			return (T) obj.copy();
		}
		catch (DatabaseValidationException e) {
			LOGGER.error("Failed to update object, reason: " + e.getMessage());
			throw new DatabaseValidationRemoteException("Failed to update object, " + e.getMessage());
		}
		catch (ObjectInstanceException e) {
			LOGGER.error("Failed to create object, reason: ", e);
			throw new ObjectInstanceRemoteException("Failed to create object, " + e.getMessage());
		}
	}
	
	@Override
	public <T extends BaseObject> void updateArray(T[] objects) throws DatabaseValidationRemoteException, ObjectInstanceRemoteException {
		try {
			objectCrudSvc.updateArray(objects);
		}
		catch (DatabaseValidationException e) {
			LOGGER.error("Failed to update object", e);
			throw new DatabaseValidationRemoteException("Failed to update object");
		}
		catch (ObjectInstanceException e) {
			LOGGER.error("Failed to update object", e);
			throw new ObjectInstanceRemoteException("Failed to update object");
		}
	}
	
	@Override
	public <T extends BaseObject> T delete(T object) throws ObjectInstanceRemoteException, DatabaseValidationRemoteException, ObjectNotFoundRemoteException {
		try {
			T obj = objectCrudSvc.delete(object);
			return (T) obj.copy();
		}
		catch (DatabaseValidationException e) {
			LOGGER.error("Failed to delete object", e);
			throw new DatabaseValidationRemoteException("Failed to delete object");
		}
		catch (ObjectInstanceException e) {
			LOGGER.error("Failed to delete object", e);
			throw new ObjectInstanceRemoteException("Failed to delete object");
		}
		catch (ObjectNotFoundException e) {
			LOGGER.error("Failed to delete object", e);
			throw new ObjectNotFoundRemoteException("Failed to delete object");
		}
	}
	
	@Override
	public BaseObject read(final String objId)  throws ObjectNotFoundRemoteException{
		try {
			LOGGER.debug("Crud REMOTE READ called " + objId);
			BaseObject object = objectCrudSvc.read(objId);
			if (object == null) {
				LOGGER.error("Failed to find object '" + objId + "'");
				throw new ObjectNotFoundRemoteException("Failed to find object '" + objId + "'");
			}
			return object.copy();
		}
		catch (ObjectNotFoundException e) {
			throw new ObjectNotFoundRemoteException(e.getMessage());
		}
	}
	
	@Override
	public <T extends BaseObject> T readByClass(final String objId, final Class<T> clz) throws ObjectNotFoundRemoteException {
		try {
			LOGGER.debug("Crud REMOTE READ called " + objId + ", class " + clz);
			T object = objectCrudSvc.readByClass(objId, clz);

			if (object == null) {
				LOGGER.error("Failed to find object '" + objId + "'");
				throw new ObjectNotFoundRemoteException("Failed to find object '" + objId + "'");
			}
			return (T) object.copy();
		}
		catch (ObjectNotFoundException e) {
			throw new ObjectNotFoundRemoteException(e.getMessage());
		}
	}
}
