package com.dronedb.persistence.triggers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface UpdateTrigger {
	
	public enum PHASE {
		CREATE,
		UPDATE
	}

	public String trigger() default "";
	
	public PHASE phase() default PHASE.CREATE;
}
