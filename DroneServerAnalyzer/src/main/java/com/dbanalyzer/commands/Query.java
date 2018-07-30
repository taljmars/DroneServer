package com.dbanalyzer.commands;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.QueryRequestRemote;
import com.db.persistence.scheme.QueryResponseRemote;
import com.dbanalyzer.QuerySvcRemoteWrapper;
import com.generic_tools.Pair.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.Column;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/*
examples:
    q select m from MissionItem m
 */
@Component
public class Query implements RunnablePayload {

    @Autowired
    private QuerySvcRemoteWrapper querySvcRemote;

    private List<Pair<String, String>> usage;

    @PostConstruct
    public void init() {
        usage = new ArrayList<>();
        usage.add(new Pair<>("q","Free query"));
    }

    @Override
    public boolean isRelevant(String payload) {
        if (!payload.startsWith("q "))
            return false;

        String[] strs = payload.split(" ");
        return strs.length > 2;
    }

    @Override
    public List<Pair<String, String>> getUsage() {
        return usage;
    }

    private final static String ObjID = "ObjID";
    private final static String fromRevision = "From";
    private final static String toRevision = "To";
    private final static String Deleted = "Deleted";
    private final static String Private = "Private";
    private final static String Clz = "Class";

    @Override
    public String run(String payload) {
        String ans = "";
        try {
            QueryRequestRemote queryRequestRemote = new QueryRequestRemote();

            String queryString = payload.substring("q ".length());
            QueryResponseRemote queryResponseRemote;// = querySvcRemote.runNativeQuery(queryString);

            //List<BaseObject> objectList = queryResponseRemote.getResultList();
            //ans += getObjectToTableString(objectList);
        }
        catch (Exception e) {
//            e.printStackTrace();
            ans += "ERROR: " + e.getMessage() + "\n";
        }

        return ans;
    }

    public static String getObjectToTableString(List<BaseObject> objectList) {
        String ans = "";
        try {
            ans += "Total Objects: " + objectList.size() + "\n";

            List<String> columns = new ArrayList();
            Map<String, Integer> columnSize = new HashMap<>();
            List<Map<String, Object>> allValues = new ArrayList<>();


            List<Method> baseMethods = new ArrayList(Arrays.asList(BaseObject.class.getMethods()));

            columns.addAll(Arrays.asList(ObjID, fromRevision, toRevision, Deleted, Private, Clz));
            columnSize.put(ObjID, 36);
            columnSize.put(fromRevision, 4);
            columnSize.put(toRevision, 10);
            columnSize.put(Deleted, 7);
            columnSize.put(Private, 7);
            columnSize.put(Clz, 7);

            for (BaseObject obj : objectList) {
                Method[] methods = obj.getClass().getMethods();
                Map<String, Object> values = new HashMap<>();

                //Handle base object part
                values.put(ObjID, obj.getKeyId().getObjId());
                values.put(fromRevision, obj.getFromRevision());
                values.put(toRevision, obj.getKeyId().getToRevision());
                values.put(Deleted, obj.isDeleted() ? "T" : "");
                values.put(Private, !obj.getKeyId().getEntityManagerCtx().equals(0) ? "T" : "");
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
                        columnSize.put(key, getKeyLength(method, key));
                    }

                    values.put(key, getValue(method, obj));
                }
                allValues.add(values);
            }

            ans += "| ";
            for (String col : columns) {
                ans += String.format("%" + columnSize.get(col) + "s | ", col);
            }
            int length = ans.length() - ans.lastIndexOf('\n');
            ans += "\n";
            for (String s : Collections.nCopies(length - 2, "-")) ans += s;
            ans += "\n";

            Iterator<Map<String, Object>> itr = allValues.iterator();
            while (itr.hasNext()) {
                Map<String, Object> entry = itr.next();
                ans += "| ";
                for (String col : columns) {
                    if (entry.keySet().contains(col)) {
                        Object val = entry.get(col);
                        String stringVal = "null";
                        if (val != null)
                             stringVal = val.toString();

                        ans += String.format("%" + columnSize.get(col) + "s | ", stringVal.substring(0, Math.min(columnSize.get(col), stringVal.length())) + "");
                    } else {
                        ans += String.format("%" + columnSize.get(col) + "s | ", " ");
                    }
                    System.out.println("");
                }
                ans += "\n";
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ans;
    }

    private static Integer getKeyLength(Method method, String key) {
        if (UUID.class.isAssignableFrom(method.getReturnType()))
            return UUID.randomUUID().toString().length();

        return Math.max(5, key.length());
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
