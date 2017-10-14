package com.dronedb.persistence.validations;

import com.dronedb.persistence.validations.internal.NoPostLandOrRTLItemsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { NoPostLandOrRTLItemsValidator.class })
public @interface NoPostLandOrRTLItemsValidation {
	
	String message() default "No flight instruction can be added after Land or RTL points";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
    boolean value() default false;
}
