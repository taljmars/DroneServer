package exporter;


//Dead code for soap exporter, should be supported in the future

/*
public class SoapExporter {

    interface filter {
        boolean test(Class c);
    }

    public static void runCopier() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        for (Class clz : Plugins.servicesList) {
            PluginManifest pluginDef = (PluginManifest) clz.newInstance();
            List<String> pkgs = pluginDef.getWebServicePackage();
            for (String pkg : pkgs)
                copyPackage(pkg, c -> c.isInterface() || Exception.class.isAssignableFrom(c));
        }

        for (Class clz : Plugins.servicesList) {
            PluginManifest pluginDef = (PluginManifest) clz.newInstance();
            List<String> schemes = pluginDef.getSchemePackage();
            for (String scheme : schemes)
                copyPackage(scheme, c -> true);
        }

        for (Class clz : Plugins.servicesList) {
            PluginManifest pluginDef = (PluginManifest) clz.newInstance();
            List<String> annotatedPackages = pluginDef.getAnnotatedPackage();
            for (String annotatedPackage : annotatedPackages) {
                copyPackage(annotatedPackage, c -> c.getAnnotation(PluginManifest.WSImporter.class) != null);
            }
        }

    }

    private static void copyPackage(String packageName, filter filter) throws IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        System.out.println("Package: " + path);
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if (resource.getFile().toString().contains(".m2") || resource.getFile().toString().contains("ServerExportedServices"))
                continue;
            System.out.println("Searching path: " + resource.getFile());
            dirs.add(new File(resource.getFile()));
            //File file = new File(resource.getFile());
        }

        List<Class> classes = null;
        for (File directory : dirs) {
            classes = copyClasses(directory, packageName, filter);
        }
        System.out.println("Built " + (classes == null ? 0 :classes.size()) + " classes");
    }

    private static List<Class> copyClasses(File directory, String packageName, filter filter) throws ClassNotFoundException, IOException {
        System.out.println("Handle " + packageName);
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }

        String path = packageName.replace('.', '\\');
        String BASE_PATH = "ServerExportedServices\\src\\main\\java\\";
        Path base = Files.createDirectories(Paths.get(BASE_PATH + path + "\\"));

        File[] files = directory.listFiles();
        for (File source : files) {
            try {
                if (source.isDirectory()) {
                    assert !source.getName().contains(".");
                    // Recursive
                    List<Class> list = copyClasses(source, packageName + "." + source.getName(), filter);
                    if (list == null || list.isEmpty()) {
                        String packageName2 = packageName + "." + source.getName();
                        String path2 = packageName2.replace('.', '\\');
                        System.out.println(path2 + " is empty");
                        File f = Paths.get(BASE_PATH + path2).toFile();
                        if (f.listFiles() == null || f.listFiles().length == 0)
                            FileUtils.deleteDirectory(f);
                    }
                    else
                        classes.addAll(list);
                }
                else if (source.getName().endsWith(".class")) {
                    Class cl = Class.forName(packageName + '.' + source.getName().substring(0, source.getName().length() - 6));
                    if (filter.test(cl)) {
                        System.out.println("Copying file: " + source.toString() + " to " + base.toFile());
                        FileUtils.copyFileToDirectory(source, base.toFile());
                        classes.add(cl);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return classes;
    }

    public static void runGenerator() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        for (Class clz : Plugins.servicesList) {
            PluginManifest pluginDef = (PluginManifest) clz.newInstance();
            List<String> schemes = pluginDef.getSchemePackage();
            for (String scheme : schemes)
                handlePackages(scheme, false);
        }

        for (Class clz : Plugins.servicesList) {
            PluginManifest pluginDef = (PluginManifest) clz.newInstance();
            List<String> pkgs = pluginDef.getWebServicePackage();
            for (String pkg : pkgs)
                handlePackages(pkg, true);
        }

        List<String> lst = new ArrayList<>();
        lst.clear();
        File[] files = new File("ServerExportedServices/src/main/wsdl/").listFiles();
        if (files == null || files.length == 0)
            return;

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".wsdl"))
                lst.add(file.getName());
        }

        JavaWsdlCodeGenerator.WSDL2JavaClass(
                "ServerExportedServices/src/main/wsdl/",
                "ServerExportedServices/src/main/java/",
                lst
        );
    }

    private static void handlePackages(String pkg, boolean interfacesOnly) throws IOException, ClassNotFoundException {
        List<String> lst = new ArrayList<>();
        Class[] c = getClasses(pkg, interfacesOnly);
        for (Class cc : c){
            System.out.println(cc.getName());
            lst.add(cc.getName());
        }
        JavaWsdlCodeGenerator.JavaClass2WSDL(
                "../../../CMpub/classes/",
                "ServerExportedServices/src/main/wsdl/",
                lst);
    }

    private static Class[] getClasses(String packageName, boolean interfacesOnly)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        System.out.println("Package: " + path);
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            if (resource.getFile().toString().contains(".m2") || resource.getFile().toString().contains("ServerExportedServices"))
                continue;
            System.out.println("Searching path: " + resource.getFile());
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName, interfacesOnly));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    private static List<Class> findClasses(File directory, String packageName, boolean interfacesOnly) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                // Recursive
                //classes.addAll(findClasses(file, packageName + "." + file.getName(), interfacesOnly));
            } else if (file.getName().endsWith(".class")) {
                Class cl = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
                if (!interfacesOnly || (interfacesOnly && cl.isInterface()))
                    classes.add(cl);
            }
        }
        return classes;
    }
}
*/