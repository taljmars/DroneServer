package com.dronedb.persistence.services.internal;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//import org.eclipse.persistence.jpa.jpql.Assert;
import com.dronedb.persistence.exception.DatabaseValidationException;
import com.dronedb.persistence.scheme.DatabaseRemoteValidationException;
import com.dronedb.persistence.scheme.KeyId;
import com.generic_tools.validations.RuntimeValidator;
import com.generic_tools.validations.ValidatorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dronedb.persistence.services.DroneDbCrudSvc;
import com.dronedb.persistence.triggers.DeleteObjectTrigger;
import com.dronedb.persistence.triggers.DeleteTrigger;
import com.dronedb.persistence.triggers.DeleteTriggers;
import com.dronedb.persistence.triggers.UpdateObjectTrigger;
import com.dronedb.persistence.triggers.UpdateTrigger;
import com.dronedb.persistence.triggers.UpdateTriggers;
import com.dronedb.persistence.triggers.UpdateTrigger.PHASE;
import com.dronedb.persistence.scheme.BaseObject;
import com.dronedb.server.DroneDBServerAppConfig;

@Component
public class DroneDbCrudSvcImpl implements DroneDbCrudSvc
{
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private RuntimeValidator runtimeValidator;

	public String CheckConnection() {
		return "Inside Implementation";
	}
	
	@Transactional
	public <T extends BaseObject> T create(final Class<T> clz) {
		System.out.println("Crud CREATE called " + clz);
		try {
			T inst = clz.newInstance();
			handleUpdateTriggers(null, inst, PHASE.CREATE);
			return inst;
		} 
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Transactional
	public <T extends BaseObject> T update(T object) throws DatabaseValidationException {
		System.out.println("Crud UPDATE called " + object);
		PHASE phase;
		T oldVersion = null;

		T existingObject = findInPrivate((Class<T>) object.getClass(), object.getKeyId());
		if (existingObject == null) {
			// Search in public
			existingObject = findInPublic((Class<T>) object.getClass(), object.getKeyId());
			if (existingObject != null) {
				System.out.println("Found in public DB, make a private copy");
				existingObject = movePublicToPrivate(existingObject);
			}
		}

		if (existingObject == null) {
			// Nothing exist at all, creating it in private db
			System.out.println("Object will be written to the databse for the first time");
			ValidatorResponse validatorResponse = runtimeValidator.validate(object);
			if (validatorResponse.isFailed()) {
				System.out.println("Validation failed: " + validatorResponse);
				throw new DatabaseValidationException(validatorResponse.getMessage());
			}

			object.getKeyId().setToRevision(Integer.MAX_VALUE);
			entityManager.persist(object);
			existingObject = object;
			phase = PHASE.CREATE;
		}
		else {
			// found in private or public, existingObject is a private copy exist in DB
			ValidatorResponse validatorResponse = runtimeValidator.validate(object);
			if (validatorResponse.isFailed()) {
				System.out.println("Validation failed: " + validatorResponse);
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
		System.out.println("Updated " + mergedObject);
		return mergedObject;
	}

	private <T extends BaseObject> T movePublicToPrivate(T existingObject) {
		T privateObject = (T) existingObject.copy();
		privateObject.getKeyId().setPrivatelyModified(true);
		entityManager.persist(privateObject);
		System.out.println("Create object in private db");
		System.out.println("Public " + existingObject);
		System.out.println("Private " + privateObject);
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
		System.out.println("Crud DELETE called " + object);

		T existingPrivateObject = findInPrivate((Class<T>) object.getClass(),object.getKeyId());
		T existingPublicObject = findInPublic((Class<T>) object.getClass(),object.getKeyId());

		if (existingPublicObject == null && existingPrivateObject == null) {
			System.err.println("No object found");
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
		System.out.println("Crud READ called " + objId + ", class " + clz);

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
			
			System.out.println("Invoking Update Trigger for:  " + newInst.toString());
			System.out.println("Triggers List: " + Arrays.asList(newInst.getClass().getAnnotations()).toString());
			
			UpdateTrigger[] updateTriggersArray = updateTriggers.value();
			
			for (UpdateTrigger updateTrigger : updateTriggersArray) {
			
				if (!updateTrigger.phase().equals(phase)) {
					System.out.println("Not in relevant phase " + updateTrigger.phase());
					continue;
				}
			
				String triggerClasspath = updateTrigger.trigger();
				Class<UpdateObjectTrigger> trigger = (Class<UpdateObjectTrigger>) ClassLoader.getSystemClassLoader().loadClass(triggerClasspath);
				UpdateObjectTrigger t = trigger.newInstance();
				t.setApplicationContext(DroneDBServerAppConfig.context);
				System.out.println("Trigger executed: " + t.getClass().getSimpleName());
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

			System.out.println("Invoking Delete Trigger for:  " + inst.toString());
			System.out.println("Triggers List: " + Arrays.asList(inst.getClass().getAnnotations()).toString());
			
			DeleteTrigger[] deleteTriggersArray = deleteTriggers.value();
			
			for (DeleteTrigger deleteTrigger : deleteTriggersArray) {
				String triggerClasspath = deleteTrigger.trigger();
				Class<DeleteObjectTrigger> trigger = (Class<DeleteObjectTrigger>) ClassLoader.getSystemClassLoader().loadClass(triggerClasspath);
				DeleteObjectTrigger t = trigger.newInstance();
				t.setApplicationContext(DroneDBServerAppConfig.context);
				System.out.println("Trigger executed: " + t.getClass().getSimpleName());
				t.handleDeleteObject(inst);	
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

	private <T extends BaseObject> T findInPrivate(Class<T> clz, KeyId keyId) throws DatabaseValidationException {
		KeyId key = keyId.copy();
		key.setPrivatelyModified(true);
		key.setToRevision(Integer.MAX_VALUE);
		T obj = entityManager.find(clz, key);
		return obj;
	}

	private <T extends BaseObject> T findInPublic(Class<T> clz, KeyId keyId) throws DatabaseValidationException {
		KeyId key = keyId.copy();
		key.setPrivatelyModified(false);
		key.setToRevision(Integer.MAX_VALUE);
		T obj = entityManager.find(clz, key);
		return obj;
	}
}
