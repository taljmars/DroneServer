package com.db.persistence;

import com.plugins_manager.PluginManifest;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ServerSchemeManifest implements PluginManifest {

    private final static Logger LOGGER = Logger.getLogger(ServerSchemeManifest.class);

    @PostConstruct
    public void init() {
        LOGGER.debug("Loading ServerSchemeManifest");
    }

    @Override
    public List<String> getExceptionsPackage() {
        List<String> arr = new ArrayList<>();
        arr.add("com.db.persistence.remote_exception");
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
        arr.add("com.db.persistence.scheme");
        return arr;
    }

}
