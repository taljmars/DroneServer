package com.dronedb.triggers;

import org.springframework.context.ApplicationContext;

import com.dronedb.persistence.scheme.BaseObject;

public interface DeleteObjectTrigger {
	
	public <T extends BaseObject> void handleDeleteObject(T object);
	
	public void setApplicationContext(ApplicationContext applicationContext);

}
