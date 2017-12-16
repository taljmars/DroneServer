package com.dronedb.persistence;

import com.plugins_manager.PluginManifest;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@ComponentScan({
        "com.dronedb.persistence",
})
@Configuration
public class DroneSchemeManifest implements PluginManifest {

    private final static Logger LOGGER = Logger.getLogger(DroneSchemeManifest.class);

    @PostConstruct
    public void init() {
        LOGGER.debug("Loading DroneSchemeManifest");
    }

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
