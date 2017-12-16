package com.db.persistence.wsRest.internal;

import com.db.persistence.exception.DatabaseValidationException;
import com.db.persistence.exception.ObjectInstanceException;
import com.db.persistence.remote_exception.DatabaseValidationRemoteException;
import com.db.persistence.remote_exception.ObjectInstanceRemoteException;
import com.db.persistence.remote_exception.ObjectNotFoundRemoteException;
import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.DummyBaseObject;
import com.db.persistence.services.ObjectCrudSvc;
import com.db.persistence.wsRest.ObjectCrudRestSvcRemote;
import javassist.tools.rmi.ObjectNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.UUID;
//import java.util.UUID;

@RestController
public class ObjectCrudRestSvcRemoteImpl implements ObjectCrudRestSvcRemote
{
	private final static Logger LOGGER = Logger.getLogger(ObjectCrudRestSvcRemoteImpl.class);

	@Autowired
	private ObjectCrudSvc objectCrudSvc;
	
	@PostConstruct
	public void init() {
		Assert.notNull(objectCrudSvc,"Failed to initiate 'objectCrudSvc'");
		LOGGER.debug("Remote Service is up !" + this.getClass().getSimpleName());
	}

	@Override
	@RequestMapping(value = "/createForUser", method = RequestMethod.GET)
	@ResponseBody
	public <T extends BaseObject> ResponseEntity<T> createForUser(@RequestParam String clz, @RequestParam String userName)  throws ObjectInstanceRemoteException {
		LOGGER.debug("Crud REMOTE CREATE called '" + clz + "'");
		objectCrudSvc.setForUser(userName);
		return create(clz);
	}

	@Override
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	@ResponseBody
	public <T extends BaseObject> ResponseEntity<T> create(@RequestParam String clz)  throws ObjectInstanceRemoteException {
		LOGGER.debug("Crud REMOTE CREATE called '" + clz + "'");
		try {
			T t = (T) objectCrudSvc.create(clz).copy();
			LOGGER.debug("TALMA Crud REMOTE CREATE called " + t);
			return new ResponseEntity<T>(t, HttpStatus.OK);
		}
		catch (ObjectInstanceException e) {
			LOGGER.error("Failed to create object", e);
			throw new ObjectInstanceRemoteException("Failed to create object");
		}
	}

	@Override
	@RequestMapping(value = "/updateForUser", method = RequestMethod.POST
			,consumes={MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
//	@Transactional
	public <T extends BaseObject> ResponseEntity<T> updateForUser(@RequestBody T object, @RequestParam String userName) throws DatabaseValidationRemoteException, ObjectInstanceRemoteException {

		LOGGER.debug("Crud REMOTE UPDATE called, type: " + object.getClass().getCanonicalName() + ", object: " + object);
		objectCrudSvc.setForUser(userName);
		return update(object);
	}

	@Override
	@RequestMapping(value = "/update", method = RequestMethod.POST
					,consumes={MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public <T extends BaseObject> ResponseEntity<T> update(@RequestBody T object) throws DatabaseValidationRemoteException, ObjectInstanceRemoteException{

		LOGGER.debug("Crud REMOTE UPDATE called, type: " + object.getClass().getCanonicalName() + ", object: " + object);
		try {
			T obj = objectCrudSvc.update(object);
			return new ResponseEntity<T>((T) obj.copy(), HttpStatus.OK);
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
	@RequestMapping(value = "/updateArray", method = RequestMethod.POST)
	@ResponseBody
	public <T extends BaseObject> void updateArray(@RequestBody T[] objects) throws DatabaseValidationRemoteException, ObjectInstanceRemoteException {
		try {
			objectCrudSvc.updateArray(objects);
		}
		catch (DatabaseValidationException e) {
			LOGGER.error("Failed to update object", e);
			throw new DatabaseValidationRemoteException("Failed to update object, " + e.getMessage());
		}
		catch (ObjectInstanceException e) {
			LOGGER.error("Failed to update object", e);
			throw new ObjectInstanceRemoteException("Failed to update object, " + e.getMessage());
		}
	}

	@Override
	@RequestMapping(value = "/deleteForUser", method = RequestMethod.POST)
	@ResponseBody
	public <T extends BaseObject> ResponseEntity deleteForUser(@RequestBody T object, @RequestParam String userName) throws ObjectInstanceRemoteException, DatabaseValidationRemoteException, ObjectNotFoundRemoteException {
		objectCrudSvc.setForUser(userName);
		return delete(object);
	}


	@Override
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public <T extends BaseObject> ResponseEntity delete(@RequestBody T object) throws ObjectInstanceRemoteException, DatabaseValidationRemoteException, ObjectNotFoundRemoteException {
		try {
			T obj = objectCrudSvc.delete(object);
			return new ResponseEntity(obj.copy(), HttpStatus.OK);
		}
		catch (DatabaseValidationException e) {
			LOGGER.error("Failed to delete object", e);
			throw new DatabaseValidationRemoteException("Failed to delete object, " + e.getMessage());
		}
		catch (ObjectInstanceException e) {
			LOGGER.error("Failed to delete object", e);
			throw new ObjectInstanceRemoteException("Failed to delete object, " + e.getMessage());
		}
		catch (ObjectNotFoundException e) {
			LOGGER.error("Failed to delete object", e);
			throw new ObjectNotFoundRemoteException("Failed to delete object, " + e.getMessage());
		}
	}

	@Override
	@RequestMapping(value = "/readForUser", method = RequestMethod.GET)
	@ResponseBody
	public <T extends BaseObject> ResponseEntity<T> readForUser(@RequestParam UUID objId, @RequestParam String userName) throws ObjectNotFoundRemoteException {
		objectCrudSvc.setForUser(userName);
		return read(objId);
	}


	@Override
	@RequestMapping(value = "/read", method = RequestMethod.GET)
	@ResponseBody
	public <T extends BaseObject> ResponseEntity<T> read(@RequestParam UUID objId) throws ObjectNotFoundRemoteException {
		try {
			LOGGER.debug("Crud REMOTE READ called " + objId);
			BaseObject object = objectCrudSvc.read(objId);
			if (object == null) {
				LOGGER.error("Failed to find object '" + objId + "'");
				throw new ObjectNotFoundRemoteException("Failed to find object '" + objId + "'");
			}
			return new ResponseEntity<T>((T) object.copy(), HttpStatus.OK);
		}
		catch (ObjectNotFoundException e) {
			throw new ObjectNotFoundRemoteException(e.getMessage());
		}
	}

	@Override
	@RequestMapping(value = "/readByClassForUser", method = RequestMethod.GET)
	@ResponseBody
	public <T extends BaseObject> ResponseEntity<T> readByClassForUser(@RequestParam UUID objId, @RequestParam String clz, @RequestParam String userName) throws ObjectNotFoundRemoteException {
		objectCrudSvc.setForUser(userName);
		return readByClass(objId, clz);
	}

	@Override
	@RequestMapping(value = "/readByClass", method = RequestMethod.GET)
	@ResponseBody
	public <T extends BaseObject> ResponseEntity<T> readByClass(@RequestParam UUID objId, @RequestParam String clz) throws ObjectNotFoundRemoteException {

		try {
			LOGGER.debug("Crud REMOTE READ called '" + objId + "', class '" + clz + "'");
			T object = objectCrudSvc.readByClass(objId, (Class<T>) Class.forName(clz));

			if (object == null) {
				LOGGER.error("Failed to find object '" + objId + "'");
				throw new ObjectNotFoundRemoteException("Failed to find object '" + objId + "'");
			}
			return new ResponseEntity<T>((T) object.copy(), HttpStatus.OK);
		}
		catch (ObjectNotFoundException e) {
			throw new ObjectNotFoundRemoteException(e.getMessage());
		}
		catch (ClassNotFoundException e) {
			throw new ObjectNotFoundRemoteException("Failed to find class type '" + clz + "', " + e.getMessage());
		}
	}
}