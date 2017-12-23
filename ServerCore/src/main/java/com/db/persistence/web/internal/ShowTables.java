package com.db.persistence.web.internal;

import com.db.persistence.scheme.BaseObject;
import com.plugins_manager.PluginManifest;
import com.plugins_manager.PluginsManager;

import javax.persistence.Table;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ShowTables {

    public static List<Class> getTableClass() {
        List<Class> tables = new ArrayList<>();
        try {

            PluginsManager pluginsManager = PluginsManager.getInstance();
            List<? extends PluginManifest> pluginManifestList = pluginsManager.getPlugins();
            for (PluginManifest pluginManifest : pluginManifestList) {
                for (String scheme : pluginManifest.getSchemePackage()) {
                    Class[] classes = getClasses(scheme);
                    tables.addAll(Arrays.asList(classes));
                }
            }

            Iterator<Class> itr = tables.iterator();
            while (itr.hasNext()) {
                if (!BaseObject.class.isAssignableFrom(itr.next()))
                    itr.remove();
            }
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return tables;
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                Class clz = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
//                System.err.println(clz);
                Table annotation = (Table) clz.getAnnotation(Table.class);
                if (annotation != null) {
//                    System.err.println("adding " + clz);
                    classes.add(clz);
                }
            }
        }
        return classes;
    }
}
