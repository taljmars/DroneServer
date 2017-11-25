package com.db.persistence.objectStore;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.ObjectDeref;
import com.db.persistence.services.internal.RevisionManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.UUID;

public abstract class EntityManagerBase {

    private final static Logger LOGGER = Logger.getLogger(EntityManagerBase.class);

    protected RevisionManager revisionManager;

    public abstract Integer getId();

    public abstract <T extends BaseObject> T find(Class<T> clz, UUID uuid);

    public abstract <T extends BaseObject> T delete(T object);

    public abstract <T extends BaseObject> T update(T object);

    public <T extends BaseObject> TypedQuery<T> createNamedQuery(String queryString, Class<T> clz) {
        return getEntityManager().createNamedQuery(queryString, clz);
    }

    public Query createQuery(String queryString) {
        return getEntityManager().createQuery(queryString);
    }

    public <T extends BaseObject> Query createNativeQuery(String queryString, Class<T> clz) {
        return getEntityManager().createNativeQuery(queryString, clz);
    }

    public abstract void discard();

    public abstract void publish();

    public BaseObject find(UUID uid) {
        ObjectDeref objectDeref = find(ObjectDeref.class, uid);
        if (objectDeref == null)
            return null;
        Class<? extends BaseObject> clz = objectDeref.getClzType();
        return find(clz, uid);
    }

    public void flush(){
        getEntityManager().flush();
    }

    public <T extends BaseObject> T pull(T obj) {
        if (obj.isDeleted())
            return null;

        if (obj.getKeyId().getToRevision() != Integer.MAX_VALUE)
            return null;

        if (obj.getKeyId().getPrivatelyModified())
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

    protected abstract SimpleEntityManagerWrapper getEntityManager();

}
