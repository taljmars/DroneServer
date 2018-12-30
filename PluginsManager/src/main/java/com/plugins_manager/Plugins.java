package com.plugins_manager;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Plugins list for the server, each plugin contain web services and scheme to be loaded
 * by the server.
 * The plugins are being loaded using reflections, without actual dependencies.
 */
@ComponentScan(
        value = {
            "com.db.persistence",
            "com.db.gui.persistence",
            "com.dronedb.persistence"
            ,"com.auditdb.persistence"
        },
        useDefaultFilters = false,
        includeFilters = @ComponentScan.Filter(Configuration.class)
)
@Configuration
public class Plugins {

     private void g() {

         servicesList = new ArrayList<>();


         //com.db.persistence.ServerSchemeManifest
         //com.db.gui.persistence.GuiSchemeManifest
         //com.auditdb.persistence.AuditLogsSchemeManifest
         //com.dronedb.persistence.DroneSchemeManifest
         servicesList.add("com.db.persistence.ServerSchemeManifest");
         servicesList.add("com.db.gui.persistence.GuiSchemeManifest");
         servicesList.add("com.auditdb.persistence.AuditLogsSchemeManifest");
         servicesList.add("com.dronedb.persistence.DroneSchemeManifest");


         //Get file from resources folder
//        ClassLoader classLoader = getClass().getClassLoader();
//        File file = new File(classLoader.getResource("plugins.xml").getFile());
//        try (Scanner scanner = new Scanner(file)) {
//
//            while (scanner.hasNextLine()) {
//                String line = scanner.nextLine();
//                System.out.println("Services manifest found - '" + line + "'");
//                servicesList.add(line);
//            }
//
//            scanner.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private static Plugins inst = null;
    public static Plugins get() {
        if (inst == null) {
            inst = new Plugins();
            inst.g();
        }
        return  inst;
    }
    public List<String> servicesList;

//    static {
//        servicesList = new ArrayList<>();
//
//
//
//        servicesList.add("com.db.persistence.ServerSchemeManifest");
//        servicesList.add("com.db.gui.persistence.GuiSchemeManifest");
//        servicesList.add("com.auditdb.persistence.AuditLogsSchemeManifest");
//        servicesList.add("com.dronedb.persistence.DroneSchemeManifest");
//    }
}
