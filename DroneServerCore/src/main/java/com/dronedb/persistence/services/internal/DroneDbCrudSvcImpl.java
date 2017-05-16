package com.dronedb.persistence.services.internal;

import com.dronedb.persistence.exception.DatabaseValidationException;
import com.dronedb.persistence.exception.ObjectInstanceException;
import com.dronedb.persistence.scheme.BaseObject;
import com.dronedb.persistence.scheme.Constants;
import com.dronedb.persistence.scheme.KeyId;
import com.dronedb.persistence.scheme.ObjectDeref;
import com.dronedb.persistence.services.DroneDbCrudSvc;
import com.dronedb.persistence.triggers.*;
import com.dronedb.persistence.triggers.UpdateTrigger.PHASE;
import com.dronedb.server.DroneDBServerAppConfig;
import com.generic_tools.validations.RuntimeValidator;
import com.generic_tools.validations.ValidatorResponse;
import javassist.tools.rmi.ObjectNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
public class DroneDbCrudSvcImpl implements DroneDbCrudSvc
{
	final static Logger logger = Logger.getLogger(DroneDbCrudSvcImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private RuntimeValidator runtimeValidator;

	@Override
	@Transactional
	public <T extends BaseObject> T create(final Class<T> clz) throws ObjectInstanceException {
		logger.debug("Crud CREATE called " + clz);
		try {
			return clz.newInstance();
		} 
		catch (InstantiationException | IllegalAccessException e) {
			logger.error("Failed to create object of type " + clz, e);
			throw new ObjectInstanceException(e);
		}
	}

	@Override
	@Transactional
	public <T extends BaseObject> T update(T object) throws DatabaseValidationException, ObjectInstanceException {
		logger.debug("Crud UPDATE called " + object);
		PHASE phase;
		T oldVersion = null;

		// Handling a case where it is the first update of a public object
		T existingObject = findInPrivate((Class<T>) object.getClass(), object.getKeyId());
		if (existingObject == null) {
			// Search in public
			existingObject = findInPublic((Class<T>) object.getClass(), object.getKeyId());
			if (existingObject != null) {
				logger.debug("Found in public DB, make a private copy");
				existingObject = movePublicToPrivate(existingObject);
				// Later in this function we will treat it as private session
			}
		}

		// Handling a case were the object doesn't exist in the private not publish db.
		// This is the creation time of this object
		if (existingObject == null) {
			// Nothing exist at all, creating it in private db
			logger.debug("Object will be written to the database for the first time");
			ValidatorResponse validatorResponse = runtimeValidator.validate(object);
			if (validatorResponse.isFailed()) {
				logger.error("Validation failed: " + validatorResponse);
				throw new DatabaseValidationException(validatorResponse.getMessage());
			}

			// Setting toVersion field to represent the last version
			object.getKeyId().setToRevision(Constants.TIP_REVISION);
			entityManager.persist(object);
			existingObject = object;
			phase = PHASE.CREATE;

			// Update ObjectDeref table for future search
			CreateObjectDeref(object);
		}

		// Handling a case where we've found an object in the private session.
		else {
			// found in private or public, existingObject is a private copy exist in DB
			ValidatorResponse validatorResponse = runtimeValidator.validate(object);
			if (validatorResponse.isFailed()) {
				logger.error("Validation failed: " + validatorResponse);
				throw new DatabaseValidationException(validatorResponse.getMessage());
			}
			oldVersion = (T) existingObject.clone();
			existingObject.set(object);
			phase = PHASE.UPDATE;
		}

		// Persisting changes
		entityManager.flush();

		// Search for object in private DB, there must be an object at this point
		existingObject = findInPrivate((Class<T>) object.getClass(),existingObject.getKeyId());

		// Invoking triggers
		handleUpdateTriggers(oldVersion, existingObject, phase);

		T mergedObject = existingObject;
		logger.debug("Updated " + mergedObject);
		return mergedObject;
	}

	private <T extends BaseObject> void CreateObjectDeref(T object) throws DatabaseValidationException, ObjectInstanceException {
		if (object instanceof ObjectDeref) {
			return;
		}
		ObjectDeref objectDeref = new ObjectDeref();
		objectDeref.setKeyId(object.getKeyId());
		objectDeref.setClz(object.getClass());
		update(objectDeref);
	}

	private <T extends BaseObject> void DeleteObjectDeref(T object) throws ObjectNotFoundException, DatabaseValidationException, ObjectInstanceException {
		ObjectDeref objectDeref = readByClass(object.getKeyId().getObjId(), ObjectDeref.class);
		delete(objectDeref);
	}

	private <T extends BaseObject> T movePublicToPrivate(T existingObject) {
		T privateObject = (T) existingObject.copy();
		privateObject.getKeyId().setPrivatelyModified(true);
		entityManager.persist(privateObject);
		logger.debug("Create object in private db");
		logger.debug("Public " + existingObject);
		logger.debug("Private " + privateObject);
		return privateObject;
	}

	@Override
	@Transactional
	public void updateSet(List<? extends BaseObject> objects) throws DatabaseValidationException, ObjectInstanceException {
		for (BaseObject object : objects)
			update(object);
	}

	@Override
	@Transactional
	public void delete(BaseObject object) throws DatabaseValidationException, ObjectInstanceException, ObjectNotFoundException {
		logger.debug("Crud DELETE called " + object);

		// We first start by getting the deletion candidate from the public/private
		BaseObject existingPrivateObject = findInPrivate(object.getClass(),object.getKeyId());
		BaseObject existingPublicObject = findInPublic(object.getClass(),object.getKeyId());

		// Object doesn't exist at all
		if (existingPublicObject == null && existingPrivateObject == null) {
			logger.warn("No object found");
			return;
		}

		// The object was just created in the private session
		if (existingPublicObject == null && existingPrivateObject != null) {
			handleDeleteTriggers(existingPrivateObject);
			entityManager.remove(existingPrivateObject);
			return;
		}

		// Object exist in public db only
		if (existingPublicObject != null && existingPrivateObject == null) {
			existingPrivateObject = movePublicToPrivate(existingPublicObject);
			handleDeleteTriggers(existingPrivateObject);
			existingPrivateObject.setDeleted(true);
			entityManager.flush();
			return;
		}

		// Calling deletion trigger
		handleDeleteTriggers(existingPrivateObject);

		// Mark object as deleted
		existingPrivateObject.setDeleted(true);

		// Delete ObjectDeref
		DeleteObjectDeref(object);

		entityManager.flush();
	}

	@Override
	@Transactional
	public BaseObject read(final UUID uid) throws ObjectNotFoundException {
		ObjectDeref objectDeref = readByClass(uid, ObjectDeref.class);
		Class<? extends BaseObject> clz = objectDeref.getClz();
		return readByClass(uid, clz);
	}

	@Override
	@Transactional
	public <T extends BaseObject> T readByClass(final UUID objId, final Class<T> clz) throws ObjectNotFoundException {
		logger.debug("Crud READ called " + objId + ", class " + clz);

		// Build a key for searches
		KeyId keyId = new KeyId();
		keyId.setObjId(objId);
		keyId.setPrivatelyModified(true);

		// First search in the private db
		T object = entityManager.find(clz ,keyId);
		if (object == null) {
			// We haven't found the object in the private db, going to search in public db
			keyId.setPrivatelyModified(false);
			object = entityManager.find(clz ,keyId);
		}

		// Check if we've found any object
		if (object == null) {
			throw new ObjectNotFoundException("Failed to get object " + objId + " of type " + clz);
		}

		return object;
	}
	
	/***********************************************************************************/
	/******************************** Handling triggers ********************************/
	/***********************************************************************************/

	private <T extends BaseObject> void handleUpdateTriggers(T oldInst, T newInst, PHASE phase) throws ObjectInstanceException {
		try {
			// Check if we have any update annotation
			UpdateTriggers updateTriggers = newInst.getClass().getAnnotation(UpdateTriggers.class);
			if (updateTriggers == null)
				return;

			logger.debug("Invoking Update Trigger for:  " + newInst.toString());
			logger.debug("Triggers List: " + Arrays.asList(newInst.getClass().getAnnotations()).toString());

			// Getting update trigger arrays
			UpdateTrigger[] updateTriggersArray = updateTriggers.value();
			
			for (UpdateTrigger updateTrigger : updateTriggersArray) {

				// Check if the required phase of the trigger matches the phase we've got
				if (!updateTrigger.phase().equals(phase)) {
					logger.debug("Not in relevant phase " + updateTrigger.phase());
					continue;
				}

				// Prepare update trigger
				String triggerClasspath = updateTrigger.trigger();
				Class<UpdateObjectTrigger> trigger = (Class<UpdateObjectTrigger>) ClassLoader.getSystemClassLoader().loadClass(triggerClasspath);
				UpdateObjectTrigger t = trigger.newInstance();
				t.setApplicationContext(DroneDBServerAppConfig.context);
				logger.debug("Trigger executed: " + t.getClass().getSimpleName());
				t.handleUpdateObject(oldInst, newInst, phase);
			}
		} 
		catch (Exception e) {
			logger.error("Failed to update trigger", e);
			throw new ObjectInstanceException(e);
		}
	}

	private <T extends BaseObject> void handleDeleteTriggers(T inst) throws ObjectInstanceException {
		try {
			DeleteTriggers deleteTriggers = inst.getClass().getAnnotation(DeleteTriggers.class);
			if (deleteTriggers == null)
				return;

			logger.debug("Invoking Delete Trigger for:  " + inst.toString());
			logger.debug("Triggers List: " + Arrays.asList(inst.getClass().getAnnotations()).toString());
			
			DeleteTrigger[] deleteTriggersArray = deleteTriggers.value();
			
			for (DeleteTrigger deleteTrigger : deleteTriggersArray) {
				String triggerClasspath = deleteTrigger.trigger();
				Class<DeleteObjectTrigger> trigger = (Class<DeleteObjectTrigger>) ClassLoader.getSystemClassLoader().loadClass(triggerClasspath);
				DeleteObjectTrigger t = trigger.newInstance();
				t.setApplicationContext(DroneDBServerAppConfig.context);
				logger.debug("Trigger executed: " + t.getClass().getSimpleName());
				t.handleDeleteObject(inst);	
			}
		} 
		catch (Exception e) {
			logger.error("Failed to handle delete trigger", e);
			throw new ObjectInstanceException(e);
		}
	}

	private <T extends BaseObject> T findInPrivate(Class<T> clz, KeyId keyId) throws DatabaseValidationException {
		KeyId key = keyId.copy();
		key.setPrivatelyModified(true);
		key.setToRevision(Constants.TIP_REVISION);
		T obj = entityManager.find(clz, key);
		return obj;
	}

	private <T extends BaseObject> T findInPublic(Class<T> clz, KeyId keyId) throws DatabaseValidationException {
		KeyId key = keyId.copy();
		key.setPrivatelyModified(false);
		key.setToRevision(Constants.TIP_REVISION);
		T obj = entityManager.find(clz, key);
		return obj;

	}
}
