package com.db.persistence;

import com.plugins_manager.PluginManifest;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@ComponentScan({
        "com.db.persistence.wsRest.internal",
        "com.db.persistence",
        "com.db.server"
})
@Configuration
public class ServerCoreManifest implements PluginManifest {

    private final static Logger LOGGER = Logger.getLogger(ServerCoreManifest.class);

    @PostConstruct
    public void init() {
        LOGGER.debug("Loading ServerCoreManifest");
    }

    @Override
    public List<String> getExceptionsPackage() {
        List<String> arr = new ArrayList<>();
        return arr;
    }

    @Override
    public List<String> getWebServicePackage() {
        List<String> arr = new ArrayList<>();
        return arr;
    }

    @Override
    public List<String> getSchemePackage() {
        List<String> arr = new ArrayList<>();
        return arr;
    }

}
