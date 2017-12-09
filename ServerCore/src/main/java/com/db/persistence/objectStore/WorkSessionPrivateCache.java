package com.db.persistence.objectStore;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.KeyId;
import com.db.persistence.scheme.ObjectDeref;
import com.generic_tools.Pair.Pair;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class WorkSessionPrivateCache {

    private final static Logger LOGGER = Logger.getLogger(WorkSessionPrivateCache.class);

    private static final Boolean ACTIVE = true;

    class Bucket extends HashMap<String, Pair<KeyId, Boolean /*Deleted*/ >>{ }

    private Bucket cacheBucket;
    private Bucket cacheObjectDerefBucket;

    public WorkSessionPrivateCache(){
        cacheBucket = new Bucket();
        cacheObjectDerefBucket = new Bucket();
    }

    private Bucket getBucket(Class clz) {
        if (clz.equals(ObjectDeref.class)) {
            LOGGER.debug("Using ObjectDeref bucket");
            return cacheObjectDerefBucket;
        }

        return cacheBucket;
    }

    public KeyId get(Class clz, String key) {
        if (!ACTIVE) return null;
        Bucket bucket = getBucket(clz);

        Pair<KeyId, Boolean> pair = bucket.get(key);
        if (pair == null)
            return null;
        return pair.getFirst();
    }

    public boolean has(Class clz, String key) {
        if (!ACTIVE) return false;
        Bucket bucket = getBucket(clz);
        return bucket.get(key) != null;
    }

    public void remove(Class clz, String keyId) {
        if (!ACTIVE) return;
        Bucket bucket = getBucket(clz);
        if (bucket != null)
            bucket.remove(keyId);
    }

    public void put(Class clz, String objId, BaseObject res) {
        if (!ACTIVE) return;

        if (res.getKeyId().getEntityManagerCtx().equals(EntityManagerType.MAIN_ENTITY_MANAGER.id))
            return;

        Bucket bucket = getBucket(clz);

        if (res.isDeleted()) {
            LOGGER.debug("Push to deleted cache: " + clz.getCanonicalName() + ", " + objId);
            bucket.put(objId, new Pair<>(res.getKeyId(), true));
            return;
        }

        LOGGER.debug("Push to cache: " + clz.getCanonicalName() + ", " + objId);
        bucket.put(objId, new Pair<>(res.getKeyId(), false));
    }

    public boolean isDeleted(Class clz, String key) {
        if (!ACTIVE) return false;
        Bucket bucket = getBucket(clz);
        Pair pair = bucket.get(key);
        return (pair != null) && pair.getSecond().equals(true);
    }

    public Set<String> getAllUuids() {
        return cacheBucket.keySet();
    }


    public void flush() {
        cacheBucket.clear();
        cacheObjectDerefBucket.clear();
    }

}
