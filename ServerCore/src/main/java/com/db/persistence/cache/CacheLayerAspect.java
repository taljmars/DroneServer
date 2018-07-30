/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.persistence.cache;

import com.db.persistence.objectStore.VirtualizedEntityManager;
import com.db.persistence.scheme.BaseObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/*
    The following is the first layer cache mechanism, it a wrapper for the entity managers in the server:
    VirtualizedEntityManager, NonVirtualizedEntityManager
 */
@Aspect
public class CacheLayerAspect {

    @Autowired
    private CacheContainerManager cacheContainerManager;


    @Around("execution(* com.db.persistence.objectStore.VirtualizedEntityManager.init(..))")
    @Transactional
    public void init(ProceedingJoinPoint pjp) throws Throwable {
//        System.out.println("init (cacheContainerManager=" + cacheContainerManager + ")");
        VirtualizedEntityManager virtualizedEntityManager = (VirtualizedEntityManager) pjp.getThis();
        cacheContainerManager.create(virtualizedEntityManager.getId());
        pjp.proceed();
    }

    @Around("execution(* com.db.persistence.objectStore.VirtualizedEntityManager.find(..))")
    @Transactional
    public <T extends BaseObject> T find(ProceedingJoinPoint pjp) {
        try {
//            System.out.println("find (cacheContainerManager=" + cacheContainerManager + ")");
            Object[] args = pjp.getArgs();

            VirtualizedEntityManager that = (VirtualizedEntityManager) pjp.getThis();
            Integer ctx = that.getId();
            CacheContainer cacheContainer = cacheContainerManager.get(ctx);

            String uuid;
            if (args.length == 1) {
                uuid = (String) args[0];
//                System.out.println("uuid=" + uuid);
                if (cacheContainer.isCached(uuid)) {
//                    System.out.println("Cache HIT");
                    return cacheContainer.get(uuid);
                }
            } else {
                Class clz = (Class) args[0];
                uuid = (String) args[1];
//                System.out.println("uuid=" + uuid + " clz=" + clz);
                if (cacheContainer.isCached(uuid, clz)) {
//                    System.out.println("Cache HIT");
                    return cacheContainer.get(uuid, clz);
                }
            }

            return (T) pjp.proceed();
        }
        catch (Throwable t) {
            System.err.println("CacheLayer: Critical Error: Failed to persist object: " + t.getMessage());
            t.printStackTrace();
            System.exit(-3);
        }
        return null;
    }


    @Around("execution(* com.db.persistence.objectStore.VirtualizedEntityManager.persist(..))")
    @Transactional
    public <T extends BaseObject> void persist(ProceedingJoinPoint pjp) {
//        System.out.println("persist (cacheContainerManager=" + cacheContainerManager + ")");
        try {
            T retVal = (T) pjp.getArgs()[0];
            pjp.proceed();
//            System.out.println("CacheLayer: Actual object was updated: " + retVal);
            Integer ctx = retVal.getKeyId().getEntityManagerCtx();
            CacheContainer cacheContainer = cacheContainerManager.get(ctx);
            cacheContainer.store(retVal); // must store the attached object
            //cacheContainerManager.dump();
        }
        catch (Throwable t) {
            System.err.println("CacheLayer: Critical Error: Failed to persist object: " + t.getMessage());
            t.printStackTrace();
            System.exit(-3);
        }
    }

    @Around("execution(* com.db.persistence.objectStore.VirtualizedEntityManager.update(..))")
    @Transactional
    public <T extends BaseObject> T update(ProceedingJoinPoint pjp) {
        T retVal = null;
        try {
//            System.out.println("update (cacheContainerManager=" + cacheContainerManager + ")");
            retVal = (T) pjp.proceed();
//            System.out.println("CacheLayer: Actual object was updated: " + retVal);
            Integer ctx = retVal.getKeyId().getEntityManagerCtx();
//            System.out.println("CacheLayer: Object context: " + ctx);
            CacheContainer cacheContainer = cacheContainerManager.get(ctx);
            cacheContainer.store(retVal); // must store the attached object
            //cacheContainerManager.dump();
            return retVal;
        }
        catch (Throwable t) {
            System.err.println("CacheLayer: Critical Error: Failed to update object: " + t.getMessage());
            t.printStackTrace();
            System.exit(-3);
        }
        return retVal;
    }

    @Around("execution(* com.db.persistence.objectStore.VirtualizedEntityManager.publish(..))")
    @Transactional
    public void publish(ProceedingJoinPoint pjp) throws Throwable {
//        System.out.println("publish (cacheContainerManager=" + cacheContainerManager + ")");
        VirtualizedEntityManager that = (VirtualizedEntityManager) pjp.getThis();
        Integer ctx = that.getId();
        pjp.proceed();
//        System.out.println("CacheLayer: Clearing context");
        cacheContainerManager.delete(ctx);
    }

    @Around("execution(* com.db.persistence.objectStore.VirtualizedEntityManager.handlePublishForType(..))")
    @Transactional
    public void handlePublishForType(ProceedingJoinPoint pjp) throws Throwable {
//        System.out.println("handlePublishForType (cacheContainerManager=" + cacheContainerManager + ")");
        VirtualizedEntityManager that = (VirtualizedEntityManager) pjp.getThis();
        Integer ctx = that.getId();
        Class clz = (Class) pjp.getArgs()[0];
        if (cacheContainerManager.get(ctx).isClassCached(clz)) {
//            System.out.println("CacheLayer: Found dirty class - " + clz.getCanonicalName());
            pjp.proceed();
        }
//        else {
//            System.out.println("CacheLayer: Skip class - " + clz.getCanonicalName());
//        }
    }

    @Around("execution(* com.db.persistence.objectStore.VirtualizedEntityManager.discard(..))")
    @Transactional
    public void discard(ProceedingJoinPoint pjp) throws Throwable {
//        System.out.println("discard (cacheContainerManager=" + cacheContainerManager + ")");
        VirtualizedEntityManager that = (VirtualizedEntityManager) pjp.getThis();
        Integer ctx = that.getId();
        pjp.proceed();
        cacheContainerManager.delete(ctx);
    }

    @Around("execution(* com.db.persistence.objectStore.VirtualizedEntityManager.handleDiscardForType(..))")
    @Transactional
    public void handleDiscardForType(ProceedingJoinPoint pjp) throws Throwable {
//        System.out.println("handleDiscardForType (cacheContainerManager=" + cacheContainerManager + ")");
        VirtualizedEntityManager that = (VirtualizedEntityManager) pjp.getThis();
        Integer ctx = that.getId();
        Class clz = (Class) pjp.getArgs()[0];
        if (cacheContainerManager.get(ctx).isClassCached(clz)) {
//            System.out.println("CacheLayer: Found dirty class - " + clz.getCanonicalName());
            pjp.proceed();
        }
//        else {
//            System.out.println("CacheLayer: Skip class - " + clz.getCanonicalName());
//        }
    }

}
