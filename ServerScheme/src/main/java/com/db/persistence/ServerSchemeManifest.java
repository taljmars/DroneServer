package com.db.persistence;

import java.util.ArrayList;
import java.util.List;

public class ServerSchemeManifest implements PluginManifest {

    @Override
    public List<String> getAnnotatedPackage() {
        List<String> arr = new ArrayList<>();
//        arr.add("com.dronedb.persistence");
        arr.add("com.db.persistence.scheme");
        return arr;
    }

    @Override
    public List<String> getWebServicePackage() {
        List<String> arr = new ArrayList<>();
//        arr.add("com.dronedb.persistence.remote_exception");
        arr.add("com.db.persistence.ws");
        return arr;
    }

    @Override
    public List<String> getSchemePackage() {
        List<String> arr = new ArrayList<>();
        arr.add("com.db.persistence.scheme");
        return arr;
    }

}
