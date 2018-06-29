/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.gui.persistence;

import com.plugins_manager.PluginManifest;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@ComponentScan({
        "com.db.gui.persistence",
})
@Configuration
public class GuiSchemeManifest implements PluginManifest {

    private final static Logger LOGGER = Logger.getLogger(GuiSchemeManifest.class);

    @PostConstruct
    public void init() {
        LOGGER.debug("Loading GuiSchemeManifest");
    }


    @Override
    public List<String> getExceptionsPackage() {
        List<String> arr = new ArrayList<>();
        return arr;
    }

    @Override
    public List<String> getWebServicePackage() {
        List<String> arr = new ArrayList<>();
        arr.add("com.db.gui.persistence.ws");
        return arr;
    }

    @Override
    public List<String> getSchemePackage() {
        List<String> arr = new ArrayList<>();
        arr.add("com.db.gui.persistence.scheme");
        return arr;
    }
}
