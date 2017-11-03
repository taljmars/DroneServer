package com.db.persistence.objectStore;

import com.db.persistence.scheme.*;
import com.db.persistence.services.internal.RevisionManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.UUID;

@Component
public class VirtualizedEntityManager {

    private final static Logger LOGGER = Logger.getLogger(VirtualizedEntityManager.class);
    private boolean permission;

    @Autowired
    private RevisionManager revisionManager;

    private EntityManagerWrapper entityManagerWrapper;

    public <T extends BaseObject> VirtualizedEntityManager(EntityManager entityManager, boolean permission) {
        entityManagerWrapper = new EntityManagerWrapper(entityManager);
        this.permission = permission;
    }

    @Autowired
    private WorkSessionPrivateCache workSessionCache;

//    @Autowired
//    private EntityInformation entityInformation;

    public <T extends BaseObject> T find(Class<T> clz, UUID uuid) {
        LOGGER.debug("Searching for " + clz.getSimpleName() + " ,uid=" + uuid);

        KeyId keyId = new KeyId();
        keyId.setToRevision(Integer.MAX_VALUE);
        keyId.setPrivatelyModified(true);
        keyId.setObjId(uuid);
        T found =  entityManagerWrapper.find(clz, keyId);
        if (found != null)
            return found;

        keyId.setPrivatelyModified(false);
        return entityManagerWrapper.find(clz, keyId);
//
//
//        LOGGER.debug("Searching for " + clz.getSimpleName() + " ,uid=" + uuid);
//        if (workSessionCache.isDeleted(clz, uuid))
//            return null;
//
//        T res;
//        KeyId keyId;
//        if (workSessionCache.has(clz, uuid)) {
//            LOGGER.debug("Object found as private");
//            keyId = workSessionCache.get(clz, uuid);
//            res = entityManagerWrapper.find(clz, keyId);
//            if (res.isDeleted()) {
//                throw new javax.persistence.EntityNotFoundException("Found deleted , unexpected");
//            }
//        }
//        else {
//            keyId = new KeyId();
//            keyId.setToRevision(Integer.MAX_VALUE);
//            keyId.setPrivatelyModified(false);
//            keyId.setObjId(uuid);
//            res = entityManagerWrapper.find(clz, keyId);
//            if (res == null) {
//                LOGGER.debug("Object not found as public");
//                return null;
//            }
//            if (res.isDeleted()) {
//                throw new javax.persistence.EntityNotFoundException("Found deleted , unexpected");
//            }
//            LOGGER.debug("Object found as public");
////            if (!readonly)
////                res = movePublicToPrivate(res);
//        }
//
////        logger.debug("In work session, find " + clz + " ,key=" + uuid);
//        return res;
    }

    public <T extends BaseObject> T delete(T object) {
        LOGGER.debug("Removing: " + object);

        // We first start by getting the deletion candidate from the public/private
        T existingObject = find((Class<T>) object.getClass(),object.getKeyId().getObjId());

        // Object doesn't exist at all
        if (existingObject == null) {
            LOGGER.warn("No object found");
            throw new EntityNotFoundException("No object found");
        }

        // Check for already deleted object
        if (existingObject.isDeleted()) {
            LOGGER.error("Object is already marked as deleted, nothing to do");
            return existingObject;
        }

        // The object was just created in the private session
        if (existingObject.getKeyId().getPrivatelyModified()) {
            LOGGER.debug("Object was created in private db only");
            if (existingObject.isDeleted()) {
                LOGGER.debug("Object is already marked as deleted");
                return existingObject;
            }

            LOGGER.debug("Remove ObjectDeref");
            DeleteObjectDeref(existingObject);

            entityManagerWrapper.remove(existingObject);
            return existingObject;
        }

        // Object exist in public db only

        LOGGER.debug("Object exits in public db only");
        if (existingObject.isDeleted()) {
            LOGGER.debug("Object is already marked as deleted");
            return existingObject;
        }
        existingObject = movePublicToPrivate(existingObject);
        existingObject.setDeleted(true);
//        entityManagerWrapper.flush();
//        existingObject = entityManagerWrapper.find((Class<T>) existingObject.getClass(), existingObject.getKeyId());
        return existingObject;
    }

    private  <T extends BaseObject> void persist(T object) {
        LOGGER.debug("Persist object: " + object);
        object.getKeyId().setToRevision(Constants.TIP_REVISION);
        object.getKeyId().setPrivatelyModified(true);
        object.setDeleted(false); //TODO: Check if we need this one, wasn't tested at all
//        workSessionCache.put(object.getClass(), object.getKeyId().getObjId(), object);
//        if (entityInformation.isNew(object)) {
        entityManagerWrapper.persist(object);
//        else
//            entityManagerWrapper.merge(object);
    }

    public <T extends BaseObject> T update(T object) {
        LOGGER.debug("VEM UPDATE called " + object);

        // Handling a case where it is the first update of a public object
        T existingObject = find((Class<T>) object.getClass(), object.getKeyId().getObjId());
        if (existingObject != null && !existingObject.getKeyId().getPrivatelyModified()) {
            LOGGER.debug("Found in public DB, make a private copy");
            existingObject = movePublicToPrivate(existingObject);
            // Later in this function we will treat it as private session
        }

        // Handling a case were the object doesn't exist in the private nor publish db.
        // This is the creation time of this object
        if (existingObject == null) {
            // Nothing exist at all, creating it in private db
            LOGGER.debug("Object will be written to the database for the first time, validating object");
//            ValidatorResponse validatorResponse = runtimeValidator.validate(object);
//            if (validatorResponse.isFailed()) {
//                logger.error("Validation failed: " + validatorResponse);
//                throw new DatabaseValidationException(validatorResponse.getMessage());
//            }

            // Setting toVersion field to represent the last version
            object.getKeyId().setToRevision(Constants.TIP_REVISION);
            object.setDeleted(false); //TODO: Check if we need this one, wasn't tested at all
            entityManagerWrapper.persist(object);
            existingObject = object;

            // Update ObjectDeref table for future search
            CreateObjectDeref(object);
        }

        // Handling a case where we've found an object in the private session.
        else {
            // found in private or public, existingObject is a private copy exist in DB
//            ValidatorResponse validatorResponse = runtimeValidator.validate(object);
//            if (validatorResponse.isFailed()) {
//                logger.error("Validation failed: " + validatorResponse);
//                throw new DatabaseValidationException(validatorResponse.getMessage());
//            }
            existingObject.set(object);
        }

        // Persisting changes
//        entityManagerWrapper.flush(); //TODO: TALMA :comment for test

        // Search for object in private DB, there must be an object at this point
//        existingObject = find((Class<T>) object.getClass(),existingObject.getKeyId().getObjId());//TODO: TALMA :comment for test

        // Invoking triggers
//        handleUpdateTriggers(oldVersion, existingObject, phase);

        T mergedObject = existingObject;
        LOGGER.debug("Updated " + mergedObject);
        return mergedObject;
    }

    private <T extends BaseObject> void DeleteObjectDeref(T object) {
        LOGGER.debug("Deleting ObjectDeref for " + object);
        ObjectDeref objectDeref = find(ObjectDeref.class, object.getKeyId().getObjId());
//        if (objectDeref == null)
//            throw new EntityNotFoundException("ObjectDeref wasn't found");
        entityManagerWrapper.remove(objectDeref);
    }

    private <T extends BaseObject> void CreateObjectDeref(T object) {
        if (object instanceof ObjectDeref) {
            return;
        }

        ObjectDeref objectDeref = new ObjectDeref();
        objectDeref.setKeyId(object.getKeyId());
        objectDeref.setClzType(object.getClass());
        objectDeref.setFromRevision(revisionManager.getNextRevision());
        update(objectDeref);
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

    public void flush() {
        entityManagerWrapper.flush();
    }

    private <T extends BaseObject> T movePublicToPrivate(T existingObject) {
        T privateObject = (T) existingObject.copy();
        privateObject.getKeyId().setPrivatelyModified(true);
        persist(privateObject);

//        logger.debug("Create object in private db");
//        logger.debug("Public " + existingObject);
//        logger.debug("Private " + privateObject);
        return privateObject;
    }

    public Metamodel getMetamodel() {
        return entityManagerWrapper.getMetamodel();
    }

    public <T extends BaseObject> TypedQuery<T> createNamedQuery(String queryString, Class<T> clz) {
        return entityManagerWrapper.createNamedQuery(queryString, clz);
    }

    public Query createQuery(String queryString) {
        return entityManagerWrapper.createQuery(queryString);
    }

    public <T extends BaseObject> Query createNativeQuery(String queryString, Class<T> clz) {
        return entityManagerWrapper.createNativeQuery(queryString, clz);
    }

    /*****************************************************************************/
    /*****************************************************************************/
    /*****************************************************************************/
    /*****************************************************************************/


    public void discard() {
        Metamodel mm = getMetamodel();
        for (final ManagedType<?> managedType : mm.getManagedTypes()) {
            Class clz = managedType.getJavaType();
            LOGGER.error("Found class " + clz);

            if (!clz.isAnnotationPresent(Sessionable.class))
                continue;

            LOGGER.error("going to handle " + clz);
            handleDiscardForType(clz);
        }

        workSessionCache.flush();
    }

    public <T extends BaseObject> void handleDiscardForType(Class<T> clz) {
        Query query = createNativeQuery(buildGetPrivateQuery(clz), clz);
        List<T> objs = query.getResultList();
        for (T item : objs) {
            ObjectDeref objectDeref = find(ObjectDeref.class, item.getKeyId().getObjId());
            if (objectDeref.getKeyId().getPrivatelyModified())
                entityManagerWrapper.remove(objectDeref);
            entityManagerWrapper.remove(item);
        }
//        Set<UUID> uuidList = workSessionCache.getAllUuids();
//        for (UUID item : uuidList) {
//            ObjectDeref objectDeref = find(ObjectDeref.class, item);
//            Class clazz = objectDeref.getClzType();
//            if (objectDeref.getKeyId().getPrivatelyModified())
//                delete(objectDeref);
//            BaseObject object = find(clazz, item);
//            delete(object);
//        }
    }


    /*****************************************************************************/
    /*****************************************************************************/
    /*****************************************************************************/
    /*****************************************************************************/

    public void publish(int nextRevision) {
        Metamodel mm = getMetamodel();
        for (final ManagedType<?> managedType : mm.getManagedTypes()) {
            Class clz = managedType.getJavaType();
            LOGGER.error("Found managed class '" + clz.getCanonicalName() + "'");
            if (Modifier.isAbstract(clz.getModifiers()))
                continue;

            if (!BaseObject.class.isAssignableFrom(clz) || clz == BaseObject.class)
                continue;

            if (!clz.isAnnotationPresent(Sessionable.class))
                continue;

            LOGGER.error("going to handle '" + clz.getCanonicalName() + "' publishing");
            handlePublishForType(clz, nextRevision);
        }
        workSessionCache.flush();
    }

    private <T extends BaseObject> void handlePublishForType(Class<T> clz, int nextRevision) {
        LOGGER.debug("Handle publish for object of type '" + clz .getCanonicalName()+ "'");
        Query query = createNativeQuery(buildGetPrivateQuery(clz), clz);
        List<T> objs = query.getResultList();
        for (T item : objs) {
            T tip = getPublishedByPrivateObject(clz, item);

            if (tip != null) {
                LOGGER.debug("Tip object " + tip);
                movePrivateToPublic(item, tip, clz, nextRevision);
            }
            else {
                LOGGER.debug("Tip object not found, object is written to the public DB for the first time");
                movePrivateToPublic(item, clz, nextRevision);
            }
        }

//        Set<UUID> uuidList = workSessionCache.getAllUuids();
//        for (UUID item : uuidList) {
//            ObjectDeref objectDeref = find(ObjectDeref.class, item);
//            Class clazz = objectDeref.getClzType();
//            if (objectDeref.getKeyId().getPrivatelyModified())
//                delete(objectDeref);
//            BaseObject object = find(clazz, item);
//            delete(object);
//        }
    }

    private <T extends BaseObject> void movePrivateToPublic(T privateItem, BaseObject publicItem, Class<T> clz, int nextRevision) {
        LOGGER.debug("Moving private to existing published object");
        T publicItemDup = (T) publicItem.copy();

        // Rewrite the last tip as old version
        publicItemDup.setDeleted(privateItem.isDeleted());
        publicItemDup.setCreationDate(publicItem.getCreationDate());
        publicItemDup.setFromRevision(publicItem.getFromRevision());
        publicItemDup.getKeyId().setToRevision(nextRevision);
        publicItemDup.getKeyId().setPrivatelyModified(false);
        entityManagerWrapper.persist(publicItemDup);

        // Updating the tip to be align to the private
        if (privateItem.isDeleted()) {

            MarkObjectDeref(privateItem, nextRevision);

            LOGGER.debug("Object for deletion " + publicItemDup);
            entityManagerWrapper.remove(publicItem);
        }
        else {
            publicItem.set(privateItem);
            publicItem.setFromRevision(nextRevision);
            LOGGER.debug("Old " + publicItemDup);
            LOGGER.debug("New " + publicItem);
//            entityManagerWrapper.flush(); //TODO: TALMA :comment for test
        }

        // Clean the private
        entityManagerWrapper.remove(privateItem);
        LOGGER.debug("Done");
    }

    private void MarkObjectDeref(BaseObject item, int nextRevision) {
            ObjectDeref objectDeref = find(ObjectDeref.class, item.getKeyId().getObjId());
            if (!item.isDeleted() && !objectDeref.getKeyId().getPrivatelyModified())
                return;

            ObjectDeref objectDerefDup = objectDeref.copy();
            objectDerefDup.getKeyId().setPrivatelyModified(false);
            if (item.isDeleted())
                objectDerefDup.getKeyId().setToRevision(nextRevision);
            objectDerefDup.setDeleted(item.isDeleted());
//			logger.debug("TALMA DBG " + objectDerefDup);
            entityManagerWrapper.persist(objectDerefDup);
            entityManagerWrapper.remove(objectDeref);
//			objectDeref = objectCrudSvc.readByClass(objectDeref.getKeyId().getObjId(), ObjectDeref.class);
//			logger.debug("TALMA DBG " + objectDeref);

    }

    private <T extends BaseObject> void movePrivateToPublic(T privateItem, Class<T> clz, int nextRevision) {
        LOGGER.debug("Moving private to NON existing published object");
        T privateItemDup = (T) privateItem.copy();

        MarkObjectDeref(privateItem, nextRevision);

        // Building a new tip
        privateItemDup.setFromRevision(nextRevision);
        privateItemDup.getKeyId().setPrivatelyModified(false);
        privateItemDup.getKeyId().setToRevision(Constants.TIP_REVISION);
        entityManagerWrapper.persist(privateItemDup);

        // Clean private
        LOGGER.debug("Removing from private " + privateItem);
        entityManagerWrapper.remove(privateItem);

        LOGGER.debug("Object writing to public " + privateItemDup);

        // Test
        KeyId keyId = new KeyId();
        keyId.setPrivatelyModified(true);
        keyId.setObjId(privateItem.getKeyId().getObjId());
        keyId.setToRevision(Integer.MAX_VALUE);
        BaseObject obj;
        if ((obj = entityManagerWrapper.find(clz, keyId)) != null)
            throw new RuntimeException("Found unexpected private object: " + obj);
        keyId.setPrivatelyModified(false);
        if ((obj = entityManagerWrapper.find(clz, keyId)) == null)
            throw new RuntimeException("Failed to find public object");


        LOGGER.debug("Done");
    }


    private String buildGetPrivateQuery(Class<? extends BaseObject> objClass) {
		LOGGER.debug("Build query to get object of " + objClass.getSimpleName());
		return String.format("SELECT * FROM %s WHERE privatelyModified = true", objClass.getSimpleName().toLowerCase());
	}

    private <T extends BaseObject> T getPublishedByPrivateObject(Class<T> clz, T item) {
        LOGGER.debug("Search for published object of " + item.getKeyId());
        KeyId key = item.getKeyId().copy();
		key.setPrivatelyModified(false);
//		logger.debug("Search for publish object of key " + key);
        return entityManagerWrapper.find(clz, key);
    }

    public BaseObject find(UUID uid) {
        ObjectDeref objectDeref = find(ObjectDeref.class, uid);
        if (objectDeref == null)
            return null;
        Class<? extends BaseObject> clz = objectDeref.getClzType();
        return find(clz, uid);
    }
}
