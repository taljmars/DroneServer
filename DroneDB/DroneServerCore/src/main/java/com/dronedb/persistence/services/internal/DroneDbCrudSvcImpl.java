package com.dronedb.persistence.services.internal;

import java.util.Arrays;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//import org.eclipse.persistence.jpa.jpql.Assert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.dronedb.persistence.services.DroneDbCrudSvc;
import com.dronedb.triggers.DeleteObjectTrigger;
import com.dronedb.triggers.DeleteTrigger;
import com.dronedb.triggers.DeleteTriggers;
import com.dronedb.triggers.UpdateObjectTrigger;
import com.dronedb.triggers.UpdateTrigger;
import com.dronedb.triggers.UpdateTriggers;
import com.dronedb.triggers.UpdateTrigger.PHASE;
import com.dronedb.persistence.scheme.BaseObject;
import com.dronedb.server.AppConfig;

@Component
public class DroneDbCrudSvcImpl implements DroneDbCrudSvc
{
	@PersistenceContext
	private EntityManager entityManager;

	public String CheckConnection() {
		return "Inside Implementation";
	}
	
	@Transactional
	public <T extends BaseObject> T create(final Class<T> clz) {
		try {
			T inst = clz.newInstance();
			handleUpdateTriggers(inst, PHASE.CREATE);			
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
	public <T extends BaseObject> T update(T object) {
		T mergedObject = object;
		if (object.getObjId() != null) {
			System.out.println("Object should exist in DB, searching for " + object);
			BaseObject existingObject = entityManager.find(object.getClass() ,object.getObjId());
			if (existingObject != null) {
				System.out.println("Found object " + object + " in the DB");
				mergedObject = entityManager.merge(mergedObject);
				handleUpdateTriggers(mergedObject, PHASE.CREATE);
				return mergedObject;
			}
		}
		else {
			System.out.println("New object " + object);
		}

		entityManager.persist(mergedObject);
		handleUpdateTriggers(mergedObject, PHASE.UPDATE);
		return mergedObject;
	}
	
	@Transactional
	public <T extends BaseObject> void updateSet(Set<T> objects) {
		for (T object : objects) {
			BaseObject mergedObject = object;
			if (object.getObjId() != null) {
				BaseObject existingObject = entityManager.find(object.getClass() ,object.getObjId());
				if (existingObject != null) {
					System.out.println("Found object " + object + " in the DB");
					mergedObject = entityManager.merge(mergedObject);
					handleUpdateTriggers(mergedObject, PHASE.CREATE);
					return;
				}
			}
			entityManager.persist(mergedObject);
			handleUpdateTriggers(mergedObject, PHASE.UPDATE);
		}
	}

	@Transactional
	public <T extends BaseObject> void delete(T object) {
		handleDeleteTriggers(object);
		T mergedObject = entityManager.merge(object);
		entityManager.remove(mergedObject);
	}
	
	@Transactional
	public <T extends BaseObject> T read(final String uid) {
		//Assert.fail("Not implemeted yet");
		return null;
	}
	
	@Transactional
	public <T extends BaseObject> T readByClass(final String objId, final Class<T> clz) {
		return entityManager.find(clz ,objId);
	}
	
	/*
	 * Handling triggers
	 */
	
	@SuppressWarnings("unchecked")
	private <T extends BaseObject> void handleUpdateTriggers(T inst, PHASE phase) {
		try {
			UpdateTriggers updateTriggers = inst.getClass().getAnnotation(UpdateTriggers.class);
			if (updateTriggers == null)
				return;
			
			System.out.println(inst.toString());
			System.out.println(Arrays.asList(inst.getClass().getAnnotations()).toString());
			
			UpdateTrigger[] updateTriggersArray = updateTriggers.value();
			
			for (UpdateTrigger updateTrigger : updateTriggersArray) {
			
				if (!updateTrigger.phase().equals(phase)) {
					System.out.println("Not in " + updateTrigger.phase());
					continue;
				}
			
				String triggerClasspath = updateTrigger.trigger();
				Class<UpdateObjectTrigger> trigger = (Class<UpdateObjectTrigger>) ClassLoader.getSystemClassLoader().loadClass(triggerClasspath);
				UpdateObjectTrigger t = trigger.newInstance();
				t.setApplicationContext(AppConfig.context);
				t.handleUpdateObject(inst, phase);
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
			
			System.out.println(inst.toString());
			System.out.println(Arrays.asList(inst.getClass().getAnnotations()).toString());
			
			DeleteTrigger[] deleteTriggersArray = deleteTriggers.value();
			
			for (DeleteTrigger deleteTrigger : deleteTriggersArray) {
				String triggerClasspath = deleteTrigger.trigger();
				Class<DeleteObjectTrigger> trigger = (Class<DeleteObjectTrigger>) ClassLoader.getSystemClassLoader().loadClass(triggerClasspath);
				DeleteObjectTrigger t = trigger.newInstance();
				t.setApplicationContext(AppConfig.context);
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
