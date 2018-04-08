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
        System.out.println("TALMA In init (cacheContainerManager=" + cacheContainerManager + ")");
        VirtualizedEntityManager virtualizedEntityManager = (VirtualizedEntityManager) pjp.getThis();
        cacheContainerManager.create(virtualizedEntityManager.getId());
        pjp.proceed();
    }

    @Around("execution(* com.db.persistence.objectStore.VirtualizedEntityManager.find(..))")
    @Transactional
    public <T extends BaseObject> T find(ProceedingJoinPoint pjp) throws Throwable {
        try {
            System.out.println("TALMA In find");
            Object[] args = pjp.getArgs();

            VirtualizedEntityManager that = (VirtualizedEntityManager) pjp.getThis();
            System.out.println("TALMA VirtualizedEntityManager=" + that);
            Integer ctx = that.getId();
            CacheContainer cacheContainer = cacheContainerManager.get(ctx);

            String uuid;
            if (args.length == 1) {
                uuid = (String) args[0];
                System.out.println("TALMA uuid=" + uuid);
                if (cacheContainer.isCached(uuid)) {
                    System.out.println("HIT");
                    return cacheContainer.get(uuid);
                }
            } else {
                Class clz = (Class) args[0];
                uuid = (String) args[1];
                System.out.println("TALMA uuid=" + uuid + " clz=" + clz);
                if (cacheContainer.isCached(uuid, clz)) {
                    System.out.println("HIT");
                    return cacheContainer.get(uuid, clz);
                }
            }

            return (T) pjp.proceed();
        }
        catch (Throwable t) {
            System.err.println("Failed to persist object: " + t.getMessage());
            t.printStackTrace();
            // TODO: not exist !!!
            System.exit(-1);
        }
        return null;
    }


    @Around("execution(* com.db.persistence.objectStore.VirtualizedEntityManager.persist(..))")
    @Transactional
    public <T extends BaseObject> void persist(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("TALMA In persist");
        try {
            T retVal = (T) pjp.getArgs()[0];
            pjp.proceed();
            System.out.println("TALMA actual object was updated: " + retVal);
            Integer ctx = retVal.getKeyId().getEntityManagerCtx();
            CacheContainer cacheContainer = cacheContainerManager.get(ctx);
            cacheContainer.store(retVal); // must store the attached object
            //cacheContainerManager.dump();
        }
        catch (Throwable t) {
            System.err.println("Failed to persist object: " + t.getMessage());
            t.printStackTrace();
            // TODO: not exist !!!
            System.exit(-1);
        }
    }

    @Around("execution(* com.db.persistence.objectStore.VirtualizedEntityManager.update(..))")
    @Transactional
    public <T extends BaseObject> T update(ProceedingJoinPoint pjp) throws Throwable {
        T retVal = null;
        try {
            System.out.println("TALMA In update");
            retVal = (T) pjp.proceed();
            System.out.println("TALMA actual object was updated: " + retVal);
            Integer ctx = retVal.getKeyId().getEntityManagerCtx();
            System.out.println("TALMA object context: " + ctx);
            CacheContainer cacheContainer = cacheContainerManager.get(ctx);
            cacheContainer.store(retVal); // must store the attached object
            //cacheContainerManager.dump();
            return retVal;
        }
        catch (Throwable t) {
            System.err.println("Failed to update object: " + t.getMessage());
            t.printStackTrace();
            // TODO: not exist !!!
            System.exit(-1);
        }
        return retVal;
    }

    @Around("execution(* com.db.persistence.objectStore.VirtualizedEntityManager.publish(..))")
    @Transactional
    public void publish(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("TALMA In publish");
        VirtualizedEntityManager that = (VirtualizedEntityManager) pjp.getThis();
        Integer ctx = that.getId();
        pjp.proceed();
        System.out.println("TALMA Clearing context");
        cacheContainerManager.delete(ctx);
    }

    @Around("execution(* com.db.persistence.objectStore.VirtualizedEntityManager.handlePublishForType(..))")
    @Transactional
    public void handlePublishForType(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("TALMA In handlePublishForType");
        VirtualizedEntityManager that = (VirtualizedEntityManager) pjp.getThis();
        Integer ctx = that.getId();
        Class clz = (Class) pjp.getArgs()[0];
        if (cacheContainerManager.get(ctx).isClassCached(clz)) {
            System.out.println("Found dirty class - " + clz.getCanonicalName());
            pjp.proceed();
        }
        else {
            System.out.println("Skip class - " + clz.getCanonicalName());
        }
    }

    @Around("execution(* com.db.persistence.objectStore.VirtualizedEntityManager.discard(..))")
    @Transactional
    public void discard(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("TALMA In discard");
        VirtualizedEntityManager that = (VirtualizedEntityManager) pjp.getThis();
        Integer ctx = that.getId();
        pjp.proceed();
        cacheContainerManager.delete(ctx);
    }

    @Around("execution(* com.db.persistence.objectStore.VirtualizedEntityManager.handleDiscardForType(..))")
    @Transactional
    public void handleDiscardForType(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("TALMA In handleDiscardForType");
        VirtualizedEntityManager that = (VirtualizedEntityManager) pjp.getThis();
        Integer ctx = that.getId();
        Class clz = (Class) pjp.getArgs()[0];
        if (cacheContainerManager.get(ctx).isClassCached(clz)) {
            System.out.println("Found dirty class - " + clz.getCanonicalName());
            pjp.proceed();
        }
        else {
            System.out.println("Skip class - " + clz.getCanonicalName());
        }
    }

}
