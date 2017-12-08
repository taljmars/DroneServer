package com.db.persistence.scheme;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The following annotation should be used when publish order is critical for an object
 * In an object is depending on another (by using it type in one of its fields) than
 * add this annotation with the relevant fields above it
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TargetType {

    Class clz();
}
