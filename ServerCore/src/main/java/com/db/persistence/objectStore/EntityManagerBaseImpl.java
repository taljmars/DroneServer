package com.db.persistence.objectStore;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.ObjectDeref;
import com.db.persistence.services.internal.RevisionManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public abstract class EntityManagerBaseImpl implements EntityManagerBase {

    private final static Logger LOGGER = Logger.getLogger(EntityManagerBaseImpl.class);

    protected RevisionManager revisionManager;

    protected SimpleEntityManagerWrapper entityManagerWrapper;

//    public abstract Integer getId();

    @Override
    public <T extends BaseObject> TypedQuery<T> createNamedQuery(String queryString, Class<T> clz) {
        return getEntityManager().createNamedQuery(queryString, clz);
    }

    @Override
    public Query createQuery(String queryString) {
        return getEntityManager().createQuery(queryString);
    }

    @Override
    public <T extends BaseObject> Query createNativeQuery(String queryString, Class<T> clz) {
        return getEntityManager().createNativeQuery(queryString, clz);
    }

    public abstract void discard();

    public abstract void publish();

    public BaseObject find(String uid) {
        ObjectDeref objectDeref = find(ObjectDeref.class, uid);
        if (objectDeref == null)
            return null;
        Class<? extends BaseObject> clz = objectDeref.getClzType();
        return find(clz, uid);
    }

    public void flush(){
        getEntityManager().flush();
    }

    @Override
    public <T extends BaseObject> T pull(T obj) {
        if (obj.isDeleted())
            return null;

        if (obj.getKeyId().getToRevision() != Integer.MAX_VALUE)
            return null;

        if (obj.getKeyId().getEntityManagerCtx() != EntityManagerType.MAIN_ENTITY_MANAGER.id)
            return obj;

        T updatedObject = find((Class<T>) obj.getClass(), obj.getKeyId().getObjId());
        if (updatedObject.isDeleted())
            return null;

        return updatedObject;
    }

    // Helpers
    protected <T extends BaseObject> void CreateObjectDeref(T object) {
        if (object instanceof ObjectDeref) {
            return;
        }

        ObjectDeref objectDeref = new ObjectDeref();
        objectDeref.setKeyId(object.getKeyId());
        objectDeref.setClzType(object.getClass());
        objectDeref.setFromRevision(revisionManager.getNextRevision());
        update(objectDeref);
    }

    protected  <T extends BaseObject> void DeleteObjectDeref(T object) {
        LOGGER.debug("Deleting ObjectDeref for " + object);
        ObjectDeref objectDeref = find(ObjectDeref.class, object.getKeyId().getObjId());
        if (objectDeref == null)
            throw new EntityNotFoundException("ObjectDeref wasn't found");
        getEntityManager().remove(objectDeref);
    }

    public abstract SimpleEntityManagerWrapper getEntityManager();

}
