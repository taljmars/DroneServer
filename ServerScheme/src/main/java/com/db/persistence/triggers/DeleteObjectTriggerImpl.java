package com.db.persistence.triggers;

import org.springframework.context.ApplicationContext;

public abstract class DeleteObjectTriggerImpl implements DeleteObjectTrigger {

	protected ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
