package com.dronedb.persistence.services.internal;

import com.dronedb.persistence.exception.DatabaseValidationException;
import com.dronedb.persistence.scheme.BaseObject;
import com.dronedb.persistence.scheme.Constants;
import com.dronedb.persistence.scheme.KeyId;
import com.dronedb.persistence.services.DroneDbCrudSvc;
import com.dronedb.persistence.triggers.*;
import com.dronedb.persistence.triggers.UpdateTrigger.PHASE;
import com.dronedb.server.DroneDBServerAppConfig;
import com.generic_tools.validations.RuntimeValidator;
import com.generic_tools.validations.ValidatorResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

//import org.eclipse.persistence.jpa.jpql.Assert;

@Component
public class DroneDbCrudSvcImpl implements DroneDbCrudSvc
{
	final static Logger logger = Logger.getLogger(DroneDbCrudSvcImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private RuntimeValidator runtimeValidator;

	public String CheckConnection() {
		return "Inside Implementation";
	}
	
	@Transactional
	public <T extends BaseObject> T create(final Class<T> clz) {
		logger.debug("Crud CREATE called " + clz);
		try {
			T inst = clz.newInstance();
			handleUpdateTriggers(null, inst, PHASE.CREATE);
			return inst;
		} 
		catch (InstantiationException e) {
			e.printStackTrace();
			logger.error("Failed to create object of type " + clz, e);
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
			logger.error("Failed to create object of type " + clz, e);
		}
		logger.error("Failed to create object of type " + clz);
		return null;
	}

	@Transactional
	public <T extends BaseObject> T update(T object) throws DatabaseValidationException {
		logger.debug("Crud UPDATE called " + object);
		PHASE phase;
		T oldVersion = null;

		T existingObject = findInPrivate((Class<T>) object.getClass(), object.getKeyId());
		if (existingObject == null) {
			// Search in public
			existingObject = findInPublic((Class<T>) object.getClass(), object.getKeyId());
			if (existingObject != null) {
				logger.debug("Found in public DB, make a private copy");
				existingObject = movePublicToPrivate(existingObject);
			}
		}

		if (existingObject == null) {
			// Nothing exist at all, creating it in private db
			logger.debug("Object will be written to the database for the first time");
			ValidatorResponse validatorResponse = runtimeValidator.validate(object);
			if (validatorResponse.isFailed()) {
				logger.error("Validation failed: " + validatorResponse);
				throw new DatabaseValidationException(validatorResponse.getMessage());
			}

			object.getKeyId().setToRevision(Constants.TIP_REVISION);
			entityManager.persist(object);
			existingObject = object;
			phase = PHASE.CREATE;
		}
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

		entityManager.flush();
		existingObject = findInPrivate((Class<T>) object.getClass(),existingObject.getKeyId());
		handleUpdateTriggers(oldVersion, existingObject, phase);

		T mergedObject = existingObject;
		logger.debug("Updated " + mergedObject);
		return mergedObject;
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

	@Transactional
	public <T extends BaseObject> void updateSet(Set<T> objects) {
//		for (T object : objects) {
//			BaseObject mergedObject = object;
//			if (object.getObjId() != null) {
//				BaseObject existingObject = entityManager.find(object.getClass() ,object.getObjId());
//				if (existingObject != null) {
//					System.out.println("Found object " + object + " in the DB");
//					mergedObject = entityManager.merge(mergedObject);
//					handleUpdateTriggers(mergedObject, PHASE.CREATE);
//					return;
//				}
//			}
//			entityManager.persist(mergedObject);
//			handleUpdateTriggers(mergedObject, PHASE.UPDATE);
//		}
	}

	@Transactional
	public <T extends BaseObject> void delete(T object) throws DatabaseValidationException {
		logger.debug("Crud DELETE called " + object);

		T existingPrivateObject = findInPrivate((Class<T>) object.getClass(),object.getKeyId());
		T existingPublicObject = findInPublic((Class<T>) object.getClass(),object.getKeyId());

		if (existingPublicObject == null && existingPrivateObject == null) {
			logger.warn("No object found");
			return;
		}

		if (existingPublicObject == null && existingPrivateObject != null) {
			handleDeleteTriggers(existingPrivateObject);
			entityManager.remove(existingPrivateObject);
			return;
		}

		if (existingPublicObject != null && existingPrivateObject == null) {
			existingPrivateObject = movePublicToPrivate(existingPublicObject);
			handleDeleteTriggers(existingPrivateObject);
			existingPrivateObject.setDeleted(true);
			entityManager.flush();
			return;
		}

		handleDeleteTriggers(existingPrivateObject);
		existingPrivateObject.setDeleted(true);
		entityManager.flush();
	}
	
	@Transactional
	public <T extends BaseObject> T read(final UUID uid) {
		//Assert.fail("Not implemeted yet");
		return null;
	}
	
	@Transactional
	public <T extends BaseObject> T readByClass(final UUID objId, final Class<T> clz) {
		logger.debug("Crud READ called " + objId + ", class " + clz);

		// Build a key for searches
		KeyId keyId = new KeyId();
		keyId.setObjId(objId);
		keyId.setPrivatelyModified(true);

		T object = entityManager.find(clz ,keyId);
		if (object == null) {
			keyId.setPrivatelyModified(false);
			object = entityManager.find(clz ,keyId);
		}

		return object;
	}
	
	/*
	 * Handling triggers
	 */
	
	@SuppressWarnings("unchecked")
	private <T extends BaseObject> void handleUpdateTriggers(T oldInst, T newInst, PHASE phase) {
		try {
			UpdateTriggers updateTriggers = newInst.getClass().getAnnotation(UpdateTriggers.class);
			if (updateTriggers == null)
				return;

			logger.debug("Invoking Update Trigger for:  " + newInst.toString());
			logger.debug("Triggers List: " + Arrays.asList(newInst.getClass().getAnnotations()).toString());
			
			UpdateTrigger[] updateTriggersArray = updateTriggers.value();
			
			for (UpdateTrigger updateTrigger : updateTriggersArray) {
			
				if (!updateTrigger.phase().equals(phase)) {
					logger.debug("Not in relevant phase " + updateTrigger.phase());
					continue;
				}
			
				String triggerClasspath = updateTrigger.trigger();
				Class<UpdateObjectTrigger> trigger = (Class<UpdateObjectTrigger>) ClassLoader.getSystemClassLoader().loadClass(triggerClasspath);
				UpdateObjectTrigger t = trigger.newInstance();
				t.setApplicationContext(DroneDBServerAppConfig.context);
				logger.debug("Trigger executed: " + t.getClass().getSimpleName());
				t.handleUpdateObject(oldInst, newInst, phase);
			}
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		catch (InstantiationException e) {
			e.printStackTrace();
		} 
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T extends BaseObject> void handleDeleteTriggers(T inst) {
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
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			logger.error("Failed to handle delete trigger", e);
		} 
		catch (InstantiationException e) {
			e.printStackTrace();
			logger.error("Failed to handle delete trigger", e);
		} 
		catch (IllegalAccessException e) {
			e.printStackTrace();
			logger.error("Failed to handle delete trigger", e);
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
