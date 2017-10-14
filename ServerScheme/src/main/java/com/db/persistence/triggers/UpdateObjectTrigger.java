package com.db.persistence.triggers;

import org.springframework.context.ApplicationContext;

import com.db.persistence.triggers.UpdateTrigger.PHASE;
import com.db.persistence.scheme.BaseObject;

public interface UpdateObjectTrigger {
	
	public <T extends BaseObject> void handleUpdateObject(T oldInst, T newInst, PHASE phase) throws Exception;
	
	public void setApplicationContext(ApplicationContext applicationContext);

}
