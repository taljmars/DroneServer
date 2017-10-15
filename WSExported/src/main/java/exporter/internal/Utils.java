package exporter.internal;

import javassist.ClassPool;
import javassist.NotFoundException;

public class Utils {

    private static ClassPool pool;

    static {
        pool = ClassPool.getDefault();
//        try {
//            pool.insertClassPath("C:\\Users\\taljmars\\Workspace\\DroneServer\\WSExported\\gen\\");
//        }
//        catch (NotFoundException e) {
//            e.printStackTrace();
//        }
    }

    public static ClassPool getPool() {return pool;}

}
