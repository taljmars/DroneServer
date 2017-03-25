package com.dronedb.triggers;

import org.springframework.context.ApplicationContext;

import com.dronedb.triggers.UpdateTrigger.PHASE;
import com.dronedb.persistence.scheme.BaseObject;

public interface UpdateObjectTrigger {
	
	public <T extends BaseObject> void handleUpdateObject(T oldInst, T newInst, PHASE phase);
	
	public void setApplicationContext(ApplicationContext applicationContext);

}
