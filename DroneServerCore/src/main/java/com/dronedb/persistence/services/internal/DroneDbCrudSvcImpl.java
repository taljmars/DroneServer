package com.dronedb.persistence.services.internal;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//import org.eclipse.persistence.jpa.jpql.Assert;
import com.dronedb.persistence.exception.DatabaseValidationException;
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
		T existingObject = entityManager.find((Class<T>) object.getClass(),object.getObjId());
		if (existingObject == null) {
			ValidatorResponse validatorResponse = runtimeValidator.validate(object);
			if (validatorResponse.isFailed()) {
				System.out.println("Validation failed: " + validatorResponse);
				throw new DatabaseValidationException(validatorResponse.toString());
			}
			entityManager.persist(object);
			phase = PHASE.CREATE;
		}
		else {
			ValidatorResponse validatorResponse = runtimeValidator.validate(object);
			if (validatorResponse.isFailed()) {
				System.out.println("Validation failed: " + validatorResponse);
				throw new DatabaseValidationException(validatorResponse.toString());
			}
			oldVersion = (T) existingObject.clone();
			existingObject.set(object);
			phase = PHASE.UPDATE;
		}

		entityManager.flush();
		existingObject = entityManager.find((Class<T>) object.getClass(),object.getObjId());
		handleUpdateTriggers(oldVersion, existingObject, phase);

		T mergedObject = (T) existingObject;
		System.out.println("Updated " + mergedObject);
		return mergedObject;
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
	public <T extends BaseObject> void delete(T object) {
		System.out.println("Crud DELETE called " + object);
		T existingObject = entityManager.find((Class<T>) object.getClass(),object.getObjId());
		handleDeleteTriggers(existingObject);
		System.out.println("Removing " + existingObject);
		entityManager.remove(existingObject);
	}
	
	@Transactional
	public <T extends BaseObject> T read(final UUID uid) {
		//Assert.fail("Not implemeted yet");
		return null;
	}
	
	@Transactional
	public <T extends BaseObject> T readByClass(final UUID objId, final Class<T> clz) {
		System.out.println("Crud READ called " + objId + ", class " + clz);
		return entityManager.find(clz ,objId);
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
}
