package com.dronedb.persistence.triggers;

import org.springframework.context.ApplicationContext;

import com.dronedb.persistence.services.DroneDbCrudSvc;
import com.dronedb.persistence.services.QuerySvc;
import com.dronedb.triggers.DeleteObjectTrigger;
import com.dronedb.persistence.scheme.BaseObject;
import com.dronedb.persistence.scheme.mission.Mission;

public abstract class DeleteObjectTriggerImpl implements DeleteObjectTrigger {

	protected ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
