package com.dronedb.persistence.validations.internal;

import com.dronedb.persistence.scheme.BaseObject;
import com.dronedb.persistence.scheme.Mission;
import com.dronedb.persistence.validations.NameNotEmpty;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by oem on 4/5/17.
 */
public class NameNotEmptyValidator implements ConstraintValidator<NameNotEmpty, Mission> {
    @Override
    public void initialize(NameNotEmpty constraintAnnotation) {

    }

    @Override
    public boolean isValid(Mission value, ConstraintValidatorContext context) {
        if (value.getName() == null || value.getName().isEmpty())
            return false;
        return true;
    }
}
