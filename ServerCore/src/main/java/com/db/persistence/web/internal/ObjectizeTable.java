package com.db.persistence.web.internal;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.services.QuerySvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/*
examples:
    q select m from MissionItem m
 */
@Component
public class ObjectizeTable {

    @Autowired
    private QuerySvc querySvc;

    private final static String ObjID = "ObjID";
    private final static String fromRevision = "From";
    private final static String toRevision = "To";
    private final static String Deleted = "Deleted";
    private final static String Private = "Private";
    private final static String Clz = "Class";

    public List<List<String>> run(String queryString) {
        List<List<String>> ans = null;
        try {
            List<? extends BaseObject> objectList = querySvc.runNativeQuery(queryString);
            ans = getObjectToTableString((List<BaseObject>) objectList);
        }
        catch (Exception e) {
//            e.printStackTrace();
        }

        return ans;
    }

    public static List<List<String>> getObjectToTableString(List<BaseObject> objectList) {
        List<List<String>> ans = new ArrayList<>();
        try {
            List<String> columns = new ArrayList();
            List<Map<String, Object>> allValues = new ArrayList<>();


            List<Method> baseMethods = new ArrayList(Arrays.asList(BaseObject.class.getMethods()));

            columns.addAll(Arrays.asList(ObjID, fromRevision, toRevision, Deleted, Private, Clz));

            for (BaseObject obj : objectList) {
                Method[] methods = obj.getClass().getMethods();
                Map<String, Object> values = new HashMap<>();

                //Handle base object part
                values.put(ObjID, obj.getKeyId().getObjId());
                values.put(fromRevision, obj.getFromRevision());
                values.put(toRevision, obj.getKeyId().getToRevision());
                values.put(Deleted, obj.isDeleted() ? "T" : "");
                values.put(Private, obj.getKeyId().getPrivatelyModified() ? "T" : "");
                String clzName = obj.getClass().getTypeName();
//                values.put(Clz, clzName.substring(clzName.lastIndexOf(".") + 1));
                values.put(Clz, obj.getClass().getSimpleName());

                for (Method method : methods) {
                    if (!method.getName().startsWith("get") && !method.getName().startsWith("is"))
                        continue;

                    if (method.getParameters().length != 0)
                        continue;

                    if (baseMethods.contains(method))
                        continue;

                    String key = getKey(method);

                    if (!columns.contains(key)) {
                        columns.add(key);
                    }

                    values.put(key, getValue(method, obj));
                }
                allValues.add(values);
            }

            ans.addAll(post(columns, allValues));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ans;
    }

    protected static List<List<String>> post(List<String> columns, List<Map<String, Object>> allValues) {
        List<List<String>> ans = new ArrayList<>();

        List<String> vals = new ArrayList<>();
        for (String col : columns) {
            vals.add(col);
        }
        ans.add(vals);

        Iterator<Map<String, Object>> itr = allValues.iterator();
        while (itr.hasNext()) {
            Map<String, Object> entry = itr.next();
            vals = new ArrayList<>();
            for (String col : columns) {
                if (entry.keySet().contains(col)) {
                    Object val = entry.get(col);
                    String stringVal = "null";
                    if (val != null)
                        stringVal = val.toString();

                    vals.add(stringVal);
                } else {
                    vals.add("");
                }
            }
            ans.add(vals);
        }
        return ans;
    }

    private static String getKey(Method method) {
        String key;
        if (method.getName().startsWith("get"))
            key = method.getName().substring("get".length());
        else
            key = method.getName().substring("is".length());

        Column c = method.getAnnotation(Column.class);
        if (c != null && !c.name().isEmpty()) {
            key = c.name();
        }

        if (Collection.class.isAssignableFrom(method.getReturnType())) {
            key += "[Size]";
        }

        if (key.isEmpty())
            throw new RuntimeException("Key is empty for method "+ method);

        return key;
    }

    private static Object getValue(Method method, Object obj) throws InvocationTargetException, IllegalAccessException {
        Object ret = method.invoke(obj, null);
        if (ret instanceof Collection)
            return ((Collection) ret).size();

        if (ret instanceof Class)
            return ((Class) ret).getSimpleName();

        return ret;
    }
}
