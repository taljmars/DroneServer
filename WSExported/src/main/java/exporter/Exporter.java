package exporter;

import com.plugins_manager.PluginManifest;
import com.plugins_manager.PluginsManager;
import exporter.internal.ExceptionHandler;
import exporter.internal.Filter;
import exporter.internal.SchemeHandler;
import exporter.internal.WebServiceHandler;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Exporter {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, IOException, ClassNotFoundException {
        String target = "ServerExportedServices" + File.separator +
                        "src" + File.separator +
                        "main" + File.separator +
                        "java" + File.separator;

        if (args.length == 1) {
            target = args[0];
            System.out.println("Received target directory as " + target);
        }

        target = System.getProperty("user.dir") + File.separator + ".." + File.separator + target;
        System.out.println("System actual directory: " + target);
        System.out.println("Cleaning the following directory from old files: " + target);
        File mainDir = new File(target);
        File[] files = mainDir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory())
                    FileUtils.deleteDirectory(f);
                else
                    Files.delete(f.toPath());
            }
        }


        PluginsManager pluginsManager = PluginsManager.getInstance();

        run(target, pluginsManager.getPlugins());
    }

    public static void run(String targetDir, List<? extends PluginManifest> servicesList) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        for (PluginManifest pluginDef : servicesList) {
            List<String> webServices = pluginDef.getWebServicePackage();
            for (String ws : webServices)
                handlePackage(ws, new WebServiceHandler(targetDir));
        }

        for (PluginManifest pluginDef : servicesList) {
            List<String> schemes = pluginDef.getSchemePackage();
            for (String scheme : schemes)
                handlePackage(scheme, new SchemeHandler(targetDir));
        }

        for (PluginManifest pluginDef : servicesList) {
            List<String> exceptions = pluginDef.getExceptionsPackage();
            for (String exception : exceptions)
                handlePackage(exception, new ExceptionHandler(targetDir));
        }
    }

    private static void handlePackage(String packageName, Filter filter) throws IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        System.out.println("Package: " + path);
        Enumeration<URL> resources = classLoader.getResources(path);
	System.out.println("resources: " + resources);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File file = new File(resource.getFile());
            dirs.add(file);
            System.out.println("Directory was added for scan: " + file.toString());
        }

        List<Class> classes = new ArrayList<>();
        for (File directory : dirs)
            classes.addAll(handleClassesInPackage(directory, packageName, filter));

        System.out.println("Built " + classes.size() + " classes");
    }

    private static List<Class> handleClassesInPackage(File directory, String packageName, Filter filter) throws ClassNotFoundException, IOException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists())
            return classes;

        File[] files = directory.listFiles();
        for (File source : files) {
            try {
                if (source.isDirectory()) {
                    assert !source.getName().contains(".");
                    // Recursive
                    List<Class> list = handleClassesInPackage(source, packageName + "." + source.getName(), filter);
                    if (list != null)
                        classes.addAll(list);
                }
                else if (source.getName().endsWith(".class")) {
                    Class cl = Class.forName(packageName + '.' + source.getName().substring(0, source.getName().length() - 6));
                    if (filter.filter(cl)) {
                        System.out.println("File was extracted and modified: " + cl.getCanonicalName());
                        classes.add(cl);
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return classes;
    }

}
