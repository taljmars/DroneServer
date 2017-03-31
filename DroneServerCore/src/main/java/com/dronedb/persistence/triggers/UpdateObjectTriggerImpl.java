package com.dronedb.persistence.triggers;

import org.springframework.context.ApplicationContext;
import com.dronedb.persistence.triggers.UpdateObjectTrigger;

public abstract class UpdateObjectTriggerImpl implements UpdateObjectTrigger {

	protected ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
