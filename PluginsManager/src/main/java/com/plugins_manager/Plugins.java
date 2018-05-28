package com.plugins_manager;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Plugins list for the server, each plugin contain web services and scheme to be loaded
 * by the server.
 * The plugins are being loaded using reflections, without actual dependencies.
 */
@ComponentScan(
        value = {
            "com.db.persistence",
            "com.db.gui.persistence",
            "com.dronedb.persistence",
            "com.auditdb.persistence"
        },
        useDefaultFilters = false,
        includeFilters = @ComponentScan.Filter(Configuration.class)
)
@Configuration
public class Plugins {

    public static List<String> servicesList;

    static {
        servicesList = new ArrayList<>();

        servicesList.add("com.db.persistence.ServerSchemeManifest");
        servicesList.add("com.db.gui.persistence.GuiSchemeManifest");
        servicesList.add("com.auditdb.persistence.AuditLogsSchemeManifest");
        servicesList.add("com.dronedb.persistence.DroneSchemeManifest");
    }
}
