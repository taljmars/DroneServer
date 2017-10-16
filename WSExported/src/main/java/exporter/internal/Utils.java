package exporter.internal;

import javassist.ClassPool;
import javassist.NotFoundException;

public class Utils {

    private static ClassPool pool;

    static {
        pool = ClassPool.getDefault();
    }

    public static ClassPool getPool() {return pool;}

}
