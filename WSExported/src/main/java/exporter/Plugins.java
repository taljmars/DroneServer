package exporter;

import com.db.persistence.ServerSchemeManifest;

import java.util.ArrayList;
import java.util.List;

public class Plugins {

    public static List<Class> servicesList;

    static {
        servicesList = new ArrayList<>();

        servicesList.add(ServerSchemeManifest.class);
//        servicesList.add(DroneSchemeManifest.class);
    }

}
