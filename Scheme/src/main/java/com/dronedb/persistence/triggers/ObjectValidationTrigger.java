package com.dronedb.persistence.triggers;

import com.dronedb.persistence.scheme.BaseObject;
import com.dronedb.persistence.triggers.UpdateTrigger.PHASE;
import org.springframework.context.ApplicationContext;

public interface ObjectValidationTrigger {
	
	public <T extends BaseObject> void ValidateObject(T oldInst, T newInst, PHASE phase);
	
	public void setApplicationContext(ApplicationContext applicationContext);

}
