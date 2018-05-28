/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.persistence.scheme;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TargetExcludeTypes {
        Class[] classes();
}
