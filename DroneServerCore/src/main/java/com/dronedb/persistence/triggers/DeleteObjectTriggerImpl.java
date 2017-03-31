package com.dronedb.persistence.triggers;

import org.springframework.context.ApplicationContext;

import com.dronedb.persistence.triggers.DeleteObjectTrigger;

public abstract class DeleteObjectTriggerImpl implements DeleteObjectTrigger {

	protected ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
