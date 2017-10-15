package com.db.persistence;

import java.util.List;

public interface PluginManifest {

    List<String> getExceptionsPackage();

    List<String> getWebServicePackage();

    List<String> getSchemePackage();
}
