package com.db.persistence.triggers;

import org.springframework.context.ApplicationContext;

public abstract class UpdateObjectTriggerImpl implements UpdateObjectTrigger {

	protected ApplicationContext applicationContext;

	public UpdateObjectTriggerImpl() {
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
