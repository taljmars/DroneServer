package com.dronedb.persistence.validations.internal;

import com.dronedb.persistence.scheme.Mission;
import com.dronedb.persistence.validations.NameNotEmptyValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by taljmars on 4/5/17.
 */
public class NameNotEmptyValidator implements ConstraintValidator<NameNotEmptyValidation, Mission> {
    @Override
    public void initialize(NameNotEmptyValidation constraintAnnotation) {
        System.err.print("Initialize validator " + getClass().getSimpleName());
    }

    @Override
    public boolean isValid(Mission value, ConstraintValidatorContext context) {
        if (value.getName() == null || value.getName().isEmpty())
            return false;
        return true;
    }
}
