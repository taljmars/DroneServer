package com.db.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

public interface PluginManifest {

    @Target(value = ElementType.TYPE)
    @Retention(value = RetentionPolicy.RUNTIME)
    @interface WSImporter {}

    List<String> getExceptionsPackage();

    List<String> getWebServicePackage();

    List<String> getSchemePackage();
}
