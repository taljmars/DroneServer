/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.persistence.services.internal;

import com.db.persistence.exception.DatabaseValidationException;
import com.db.persistence.exception.ObjectInstanceException;
import com.db.persistence.scheme.BaseObject;
import com.db.persistence.services.ObjectCrudSvc;
import com.db.persistence.triggers.*;
import com.db.persistence.triggers.UpdateTrigger.PHASE;
import com.db.server.ServerMain;
import com.generic_tools.validations.RuntimeValidator;
import com.generic_tools.validations.ValidatorResponse;
import javassist.tools.rmi.ObjectNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ObjectCrudSvcImpl extends TokenAwareSvcImpl<ObjectCrudSvc> implements ObjectCrudSvc
{
	private final static Logger LOGGER = Logger.getLogger(ObjectCrudSvcImpl.class);

	@Autowired
	private RuntimeValidator runtimeValidator;

	@PostConstruct
	public void init() {
		LOGGER.debug("Initialize ObjectCrudSvc");
	}

	@Override
	@Transactional
	public <T extends BaseObject> T create(String clz) throws ObjectInstanceException {
		LOGGER.debug("Crud CREATE called " + clz);
		try {
			Class<T> c = (Class<T>) Class.forName(clz);
			T inst = c.newInstance();
			handleUpdateTriggers(null, inst, PHASE.PRE_PERSIST);
			return inst;
		} 
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			LOGGER.error("Failed to create object of type " + clz, e);
			throw new ObjectInstanceException(e);
		}
	}

	@Override
	@Transactional
	public <T extends BaseObject> T update(T object) throws DatabaseValidationException, ObjectInstanceException {
		LOGGER.debug("Crud UPDATE called " + object);
		PHASE phase;

		LOGGER.debug("Search for the current object");
		T oldVersion = (T) read(object.getKeyId().getObjId());
		if (oldVersion != null) {
			LOGGER.debug("Found existing object, clone it");
			oldVersion = (T) oldVersion.clone();
		}

		LOGGER.debug("Run validation on the object");
		ValidatorResponse validatorResponse = runtimeValidator.validate(object);
		if (validatorResponse.isFailed()) {
			LOGGER.error("Validation failed: " + validatorResponse);
			throw new DatabaseValidationException(validatorResponse.getMessage());
		}

		LOGGER.debug("Going to update object");
		T existingObject = workSession().update(object);

		LOGGER.debug("Handling triggers");
		handleUpdateTriggers(oldVersion, existingObject, oldVersion == null ? PHASE.CREATE : PHASE.UPDATE );

		T mergedObject = existingObject;
		LOGGER.debug("Updated " + mergedObject);
		return mergedObject;
	}


	@Override
	@Transactional
	public void updateArray(BaseObject[] objects) throws DatabaseValidationException, ObjectInstanceException {
		for (BaseObject object : objects)
			update(object);
	}

	@Override
	@Transactional
	public <T extends BaseObject> T delete(T object) throws DatabaseValidationException, ObjectInstanceException, ObjectNotFoundException {
		LOGGER.debug("Crud DELETE called " + object);

		T existingPrivateObject = workSession().delete(object);
		if (existingPrivateObject == null) {
			throw new ObjectInstanceException("Object wasn't found");
		}

		// Calling deletion trigger
		LOGGER.debug("Calling delete triggers");
		handleDeleteTriggers(existingPrivateObject);

		workSession().flush();

		return existingPrivateObject;
	}

	@Override
	@Transactional
	public BaseObject read(final String uid) {
		return workSession().find(uid);
	}

	@Override
	@Transactional
	public <T extends BaseObject> T readByClass(String objId, Class<T> clz) throws ObjectNotFoundException {
		LOGGER.debug("Crud READ called '" + objId + "', class '" + clz.getSimpleName() + "'");

		// First search in the private db
		T object = workSession().find(clz ,objId);
		return object;
	}

	/***********************************************************************************/
	/******************************** Handling triggers ********************************/
	/***********************************************************************************/

	private <T extends BaseObject> void handleUpdateTriggers(T oldInst, T newInst, PHASE phase) throws ObjectInstanceException {
		try {
			List<UpdateTrigger> updateTriggerList = new ArrayList<>();

			// Check if we have any update annotation
			UpdateTriggers updateTriggers = newInst.getClass().getAnnotation(UpdateTriggers.class);
			if (updateTriggers != null)
				updateTriggerList.addAll(Arrays.asList(updateTriggers.value()));

			// Check if we have any update annotation
			UpdateTrigger[] updateTriggersArray = newInst.getClass().getAnnotationsByType(UpdateTrigger.class);
			if (updateTriggersArray.length != 0)
				updateTriggerList.addAll(Arrays.asList(updateTriggersArray));

			if (updateTriggerList.isEmpty())
				return;

			LOGGER.debug("Invoking Update Trigger for:  " + newInst.toString());
			LOGGER.debug("Triggers List: " + Arrays.asList(newInst.getClass().getAnnotations()).toString());

			for (UpdateTrigger updateTrigger : updateTriggerList) {

				// Check if the required phase of the trigger matches the phase we've got
				if (!updateTrigger.phase().equals(phase)) {
					LOGGER.debug("Not in relevant phase " + updateTrigger.phase());
					continue;
				}

				// Prepare update trigger
				String triggerClasspath = updateTrigger.trigger();
				Class<? extends UpdateObjectTrigger> trigger = (Class<? extends UpdateObjectTrigger>) this.getClass().getClassLoader().loadClass(triggerClasspath);
				LOGGER.debug("Trigger executed: " + trigger.getSimpleName());
				UpdateObjectTrigger t = trigger.newInstance();
				t.setApplicationContext(ServerMain.context);
				t.handleUpdateObject(oldInst, newInst, phase);
			}
		} 
		catch (Exception e) {
			LOGGER.error("Failed to update trigger", e);
			throw new ObjectInstanceException(e);
		}
	}

	private <T extends BaseObject> void handleDeleteTriggers(T inst) throws ObjectInstanceException {
		try {
			List<DeleteTrigger> deleteTriggerList = new ArrayList<>();

			DeleteTriggers deleteTriggers = inst.getClass().getAnnotation(DeleteTriggers.class);
			if (deleteTriggers != null)
				deleteTriggerList.addAll(Arrays.asList(deleteTriggers.value()));

			DeleteTrigger[] deleteTriggersArray = inst.getClass().getAnnotationsByType(DeleteTrigger.class);
			if (deleteTriggersArray.length != 0)
				deleteTriggerList.addAll(Arrays.asList(deleteTriggersArray));

			if (deleteTriggerList.isEmpty())
				return;

			LOGGER.debug("Invoking Delete Trigger for:  " + inst.toString());
			LOGGER.debug("Triggers List: " + Arrays.asList(inst.getClass().getAnnotations()).toString());
			
			for (DeleteTrigger deleteTrigger : deleteTriggerList) {
				String triggerClasspath = deleteTrigger.trigger();
				Class<DeleteObjectTrigger> trigger = (Class<DeleteObjectTrigger>) this.getClass().getClassLoader().loadClass(triggerClasspath);
				DeleteObjectTrigger t = trigger.newInstance();
				t.setApplicationContext(ServerMain.context);
				LOGGER.debug("Trigger executed: " + t.getClass().getSimpleName());
				t.handleDeleteObject(inst);	
			}
		} 
		catch (Exception e) {
			LOGGER.error("Failed to handle delete trigger", e);
			throw new ObjectInstanceException(e);
		}
	}

}
