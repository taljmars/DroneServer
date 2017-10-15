package exporter;

import com.db.persistence.PluginManifest;
import com.db.persistence.ServerSchemeManifest;
import com.dronedb.persistence.DroneSchemeManifest;

import java.util.ArrayList;
import java.util.List;

public class Plugins {

    public static List<Class<? extends PluginManifest>> servicesList;

    static {
        servicesList = new ArrayList<>();

        servicesList.add(ServerSchemeManifest.class);
        servicesList.add(DroneSchemeManifest.class);
    }



}
