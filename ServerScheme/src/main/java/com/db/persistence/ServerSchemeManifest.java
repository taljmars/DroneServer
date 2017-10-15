package com.db.persistence;

import java.util.ArrayList;
import java.util.List;

public class ServerSchemeManifest implements PluginManifest {

    @Override
    public List<String> getExceptionsPackage() {
        List<String> arr = new ArrayList<>();
        arr.add("com.db.persistence.remote_exception");
        return arr;
    }

    @Override
    public List<String> getWebServicePackage() {
        List<String> arr = new ArrayList<>();
        arr.add("com.db.persistence.wsSoap");
        return arr;
    }

    @Override
    public List<String> getSchemePackage() {
        List<String> arr = new ArrayList<>();
        arr.add("com.db.persistence.scheme");
        arr.add("com.db.persistence.wsSoap");
        return arr;
    }

}
