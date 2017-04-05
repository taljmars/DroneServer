package com.dronedb.persistence.validations;

import com.dronedb.persistence.validations.internal.NameNotEmptyValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { NameNotEmptyValidator.class })
public @interface NameNotEmptyValidation {
	
	String message() default "Name value cannot be null or empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
    boolean value() default false;
}
