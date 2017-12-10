package com.plugins_manager;

import java.util.ArrayList;
import java.util.List;

/**
 * Plugins list for the server, each plugin contain web services and scheme to be loaded
 * by the server.
 * The plugins are being loaded using reflections, without actual dependencies.
 */
public class Plugins {

    public static List<String> servicesList;

    static {
        servicesList = new ArrayList<>();

        servicesList.add("com.db.persistence.ServerSchemeManifest");
        servicesList.add("com.dronedb.persistence.DroneSchemeManifest");
    }
}
