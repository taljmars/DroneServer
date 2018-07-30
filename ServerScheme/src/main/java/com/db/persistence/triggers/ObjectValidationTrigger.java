package com.db.persistence.triggers;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.triggers.UpdateTrigger.PHASE;
import org.springframework.context.ApplicationContext;

public interface ObjectValidationTrigger {
	
	<T extends BaseObject> void ValidateObject(T oldInst, T newInst, PHASE phase);
	
	void setApplicationContext(ApplicationContext applicationContext);

}
