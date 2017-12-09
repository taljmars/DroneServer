package com.plugins_manager;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PluginsManager {

    private final static Logger LOGGER = Logger.getLogger(PluginsManager.class);

    private PluginsManager(){}
    private static PluginsManager instance = null;
    static {
        instance = new PluginsManager();
        instance.init();
    }

    private List<PluginManifest> plugins;
    private List<String> schemes;
    private List<String> webServices;

    public static PluginsManager getInstance() {
        return instance;
    }

    private void init() {
        LOGGER.debug("Loading Plugin Manager");
        plugins = new ArrayList<>();
        schemes = new ArrayList<>();
        webServices = new ArrayList<>();
        try {
            for (String plugin : Plugins.servicesList) {
                Class<? extends PluginManifest> clz = (Class<? extends PluginManifest>) getClass().getClassLoader().loadClass(plugin);
                LOGGER.debug("New Plugin was loaded: " + clz.getCanonicalName());
                PluginManifest pluginDef = clz.newInstance();
                plugins.add(pluginDef);
                schemes.addAll(pluginDef.getSchemePackage());
                webServices.addAll(pluginDef.getWebServicePackage());
            }
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            LOGGER.error("Failed to load plugins", e);
        }
    }

    public List<PluginManifest> getPlugins() {
        return plugins;
    }

    public List<String> getSchemes() {
        return schemes;
    }

    public List<String> getWebServices() {
        return webServices;
    }
}
