package com.plugins_manager;

import java.util.ArrayList;
import java.util.List;

public class Plugins {

    public static List<String> servicesList;

    static {
        servicesList = new ArrayList<>();

        servicesList.add("com.db.persistence.ServerSchemeManifest");
        servicesList.add("com.dronedb.persistence.DroneSchemeManifest");
    }



}
