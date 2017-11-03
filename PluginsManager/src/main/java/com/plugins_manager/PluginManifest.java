package com.plugins_manager;

import java.util.List;

public interface PluginManifest {

    List<String> getExceptionsPackage();

    List<String> getWebServicePackage();

    List<String> getSchemePackage();
}
