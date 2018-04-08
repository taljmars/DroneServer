/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.persistence.cache;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.ObjectDeref;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@Scope("prototype")
public class CacheContainer {

    private final static Logger LOGGER = Logger.getLogger(CacheContainer.class);

    private Set<Class> dirtyClass;
    private Map<String, BaseObject> dirtyObjectMap;
    private Map<String, ObjectDeref> dirtyObjectDerefMap;

    @PostConstruct
    private void init() {
        dirtyClass = new HashSet<>();
        dirtyObjectMap = new HashMap<>();
        dirtyObjectDerefMap = new HashMap<>();
    }

    public boolean isCached(String uuid) {
        return dirtyObjectMap.containsKey(uuid);
    }

    public boolean isCached(String uuid, Class clz) {
        if (clz.equals(ObjectDeref.class))
            return dirtyObjectDerefMap.containsKey(uuid);
        return isCached(uuid);
    }

    public <T extends BaseObject> void store(T object) {
        String uuid = object.getKeyId().getObjId();
        Class clz = object.getClass();

        dirtyClass.add(clz);

        LOGGER.debug("Storing " + object);
        if (clz.equals(ObjectDeref.class))
            dirtyObjectDerefMap.put(uuid, (ObjectDeref) object);
//            return;
        else
            dirtyObjectMap.put(uuid, object);
    }
/*
    public <T extends BaseObject> void drop(T object) {
        String uuid = object.getKeyId().getObjId();
        Class clz = object.getClass();

//        dirtyObject.remove(uuid);
//        dirtyObjectType.remove(uuid);
//        dirtyClass.remove(clz);

        if (clz.equals(ObjectDeref.class))
            dirtyObjectDerefMap.remove(uuid);
        else
            dirtyObjectMap.remove(uuid);
    }
 */
    public boolean isClassCached(Class clz) {
        return dirtyClass.contains(clz);
    }

    public <T extends BaseObject> T get(String uuid) {
        return (T) dirtyObjectMap.get(uuid);
    }

    public <T extends BaseObject> T get(String uuid, Class clz) {
        if (clz.equals(ObjectDeref.class))
            return (T) dirtyObjectDerefMap.get(uuid);
        return (T) dirtyObjectMap.get(uuid);
    }

    @Override
    public String toString() {
        String res = "Dirty object (" + dirtyObjectMap.size() + "):\n";
        for (String uid : dirtyObjectMap.keySet()) {
            res += "UID: " + uid + " ,Object: " + dirtyObjectMap.get(uid) + "\n";
        }
        return res.substring(0,res.length() - 1);
    }
}
