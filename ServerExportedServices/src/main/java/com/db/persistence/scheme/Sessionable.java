package com.db.persistence.scheme;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The following annotation represent an object that need to be publish or discard.
 * Every object with this annotation will be taking into account when using publish/discard mechanism.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sessionable {
}
