package com.db.persistence.triggers;

import org.springframework.context.ApplicationContext;

import com.db.persistence.scheme.BaseObject;

public interface DeleteObjectTrigger {
	
	<T extends BaseObject> void handleDeleteObject(T object) throws Exception;
	
	void setApplicationContext(ApplicationContext applicationContext);

}
