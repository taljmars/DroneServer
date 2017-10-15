package com.dronedb.persistence;

import com.db.persistence.PluginManifest;

import java.util.ArrayList;
import java.util.List;

public class DroneSchemeManifest implements PluginManifest {

    @Override
    public List<String> getExceptionsPackage() {
        List<String> arr = new ArrayList<>();
        return arr;
    }

    @Override
    public List<String> getWebServicePackage() {
        List<String> arr = new ArrayList<>();
        arr.add("com.dronedb.persistence.ws");
        return arr;
    }

    @Override
    public List<String> getSchemePackage() {
        List<String> arr = new ArrayList<>();
        arr.add("com.dronedb.persistence.scheme");
        return arr;
    }

}
