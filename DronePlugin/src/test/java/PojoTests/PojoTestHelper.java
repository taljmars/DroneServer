package PojoTests;

import com.db.persistence.scheme.BaseObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;

public class PojoTestHelper {

    public static boolean testPojo(BaseObject object) {
//        System.out.println("POJO Test: " + object.getClass().getName());
        BaseObject tempBaseObject = object.clone();
        assertTrue(checkClone(object, tempBaseObject));
        tempBaseObject = object.copy();
        assertTrue(tempBaseObject.equals(object));
        return true;
    }

    public static boolean checkClone(BaseObject object, BaseObject clonedObject) {
        try {
            Method[] methods = object.getClass().getMethods();
            for (Method method : methods) {
                // Blacklist
                if (method.getName().equals("getKeyId"))
                    continue;

                // Actual check
                if (method.getName().startsWith("get")) {
                    Object retVal = method.invoke(object, null);
                    Object retValCloned = method.invoke(clonedObject, null);
                    if (retVal == null && retValCloned == null)
                        continue;
                    if (!retVal.equals(retValCloned)) {
                        System.err.println("Object :" + object.getClass().getName() + " | Method: " + method);
                        System.err.println("Method returned different values, orig=" + retVal + ", cloned=" + retValCloned);
                        return false;
                    }
                }
            }
            return true;
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }
}
