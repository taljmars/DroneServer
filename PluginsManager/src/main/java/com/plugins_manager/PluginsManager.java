package com.plugins_manager;

import org.apache.log4j.Logger;

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
                Class pluginClz = getClass().getClassLoader().loadClass(plugin);
                LOGGER.debug("New Plugin was found: " + pluginClz.getCanonicalName());
                Object pluginObject = pluginClz.newInstance();
                assert pluginObject instanceof PluginManifest : "Object is not a plugin manifest";

                PluginManifest pluginDef = (PluginManifest) pluginObject;
                plugins.add(pluginDef);
                schemes.addAll(pluginDef.getSchemePackage());
                webServices.addAll(pluginDef.getWebServicePackage());
            }
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            LOGGER.error("Failed to read plugins", e);
            System.err.println("Failed to read plugin, " + e.getMessage() + " (" + e.getClass().getSimpleName() + ")");
            System.exit(-1);
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
