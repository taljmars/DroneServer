package com.db.persistence.objectStore;

import com.db.persistence.scheme.*;
import com.db.persistence.services.internal.RevisionManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import java.util.List;
import java.util.UUID;

@Lazy
@Scope(scopeName = "prototype")
@Component
public class VirtualizedEntityManager extends EntityManagerBase {

    private final static Logger LOGGER = Logger.getLogger(VirtualizedEntityManager.class);

    @Autowired
    public void setRevisionManager(RevisionManager revisionManager) {
        this.revisionManager = revisionManager;
    }

    @Autowired
    private ApplicationContext applicationContext;

    protected SimpleEntityManagerWrapper entityManagerWrapper;
    protected Integer entityManagerCtx;

    public VirtualizedEntityManager(EntityManager entityManager, Integer entityManagerCtx) {
        this.entityManagerWrapper = new SimpleEntityManagerWrapper(entityManager);
        this.entityManagerCtx = entityManagerCtx;
        LOGGER.debug("Create a new VEM for id " + this.entityManagerCtx);
    }

    @Override
    public <T extends BaseObject> T find(Class<T> clz, UUID uuid) {
        LOGGER.debug("Searching for " + clz.getSimpleName() + " ,uid=" + uuid);

        KeyId keyId = new KeyId();
        keyId.setToRevision(Integer.MAX_VALUE);
        keyId.setObjId(uuid);
        keyId.setEntityManagerCtx(entityManagerCtx);
        T found =  entityManagerWrapper.find(clz, keyId);
        if (found != null) {
            LOGGER.debug("Found object in private db");
            return found;
        }

        keyId.setEntityManagerCtx(EntityManagerType.MAIN_ENTITY_MANAGER.id);
        found = entityManagerWrapper.find(clz, keyId);
        if (found != null) LOGGER.debug("Found object in public db");
        else LOGGER.debug("Object doesn't exist");

        return found;
    }

    @Override
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
        if (!existingObject.getKeyId().getEntityManagerCtx().equals(EntityManagerType.MAIN_ENTITY_MANAGER.id)) {
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
//        existingObject.setEntityManagerCtx(-1);
        return existingObject;
    }

    private  <T extends BaseObject> void persist(T object) {
        LOGGER.debug("Persist object: " + object);
        object.getKeyId().setToRevision(Constants.TIP_REVISION);
        object.getKeyId().setEntityManagerCtx(entityManagerCtx);
        object.setDeleted(false);
//        workSessionCache.put(object.getClass(), object.getKeyId().getObjId(), object);
//        if (entityInformation.isNew(object)) {
        entityManagerWrapper.persist(object);
//        else
//            entityManagerWrapper.merge(object);
    }

    @Override
    public <T extends BaseObject> T update(T object) {
        LOGGER.debug("VEM UPDATE called " + object);

        // Handling a case where it is the first update of a public object
        T existingObject = find((Class<T>) object.getClass(), object.getKeyId().getObjId());
        if (existingObject != null && existingObject.getKeyId().getEntityManagerCtx().equals(EntityManagerType.MAIN_ENTITY_MANAGER.id)) {
            LOGGER.debug("Found in public DB, make a private copy");
            existingObject = movePublicToPrivate(existingObject);
            // Later in this function we will treat it as private session
        }

        // Handling a case were the object doesn't exist in the private nor publish db.
        // This is the creation time of this object
        if (existingObject == null) {
            // Nothing exist at all, creating it in private db
            LOGGER.debug("Object will be written to the database for the first time, validating object");

            // Setting toVersion field to represent the last version
            object.getKeyId().setToRevision(Constants.TIP_REVISION);
            object.setDeleted(false);
            object.getKeyId().setEntityManagerCtx(entityManagerCtx);
//            object.setEntityManagerCtx(entityManagerCtx);
            entityManagerWrapper.persist(object);
            existingObject = object;

            // Update ObjectDeref table for future search
            CreateObjectDeref(object);
        }

        // Handling a case where we've found an object in the private session.
        else {
            existingObject.set(object);
        }

        LOGGER.debug("Updated " + existingObject);
        return existingObject;
    }

    @Override
    protected SimpleEntityManagerWrapper getEntityManager() {
        return entityManagerWrapper;
    }

    @Override
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
    }

    @Override
    public void publish() {
        int nextRevision = revisionManager.getNextRevision();

        LOGGER.debug("Start publish class types");
        List<Class> lst = applicationContext.getBean(ManagedClassTopologicalSorter.class).getManagedClasses(entityManagerWrapper);
        for (Class clz : lst) {
            LOGGER.debug("Going to handle '" + clz.getCanonicalName() + "' publishing");
            handlePublishForType(clz, nextRevision);
        }

        LOGGER.debug("Flush Entity Manager");
        entityManagerWrapper.flush();

        LOGGER.debug("Set next revision");
        revisionManager.advance();
    }

    @Override
    public Integer getId() {
        return entityManagerCtx;
    }

    /*****************************************************************************/
    /*****************************************************************************/
    /*****************************************************************************/
    /*****************************************************************************/

    private Metamodel getMetamodel() {
        return entityManagerWrapper.getMetamodel();
    }

    private <T extends BaseObject> T movePublicToPrivate(T existingObject) {
        T privateObject = (T) existingObject.copy();
        privateObject.getKeyId().setEntityManagerCtx(entityManagerCtx);
        persist(privateObject);

//        LOGGER.debug("Create object in private db");
//        LOGGER.debug("Public " + existingObject);
//        LOGGER.debug("Private " + privateObject);
        return privateObject;
    }

    private  <T extends BaseObject> void handleDiscardForType(Class<T> clz) {
        Query query = createNativeQuery(buildGetPrivateQuery(clz), clz);
        List<T> objs = query.getResultList();
        for (T item : objs) {
            ObjectDeref objectDeref = find(ObjectDeref.class, item.getKeyId().getObjId());
            if (objectDeref.getKeyId().getEntityManagerCtx() != EntityManagerType.MAIN_ENTITY_MANAGER.id)
                entityManagerWrapper.remove(objectDeref);
            entityManagerWrapper.remove(item);
        }
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
    }

    private <T extends BaseObject> void movePrivateToPublic(T privateItem, BaseObject publicItem, Class<T> clz, int nextRevision) {
        LOGGER.debug("Moving private to existing published object");
        T publicItemDup = (T) publicItem.copy();

        // Rewrite the last tip as old version
        publicItemDup.setDeleted(privateItem.isDeleted());
        publicItemDup.setCreationDate(publicItem.getCreationDate());
        publicItemDup.setFromRevision(publicItem.getFromRevision());
        publicItemDup.getKeyId().setToRevision(nextRevision);
        publicItemDup.getKeyId().setEntityManagerCtx(EntityManagerType.MAIN_ENTITY_MANAGER.id);
//        publicItemDup.setEntityManagerCtx(privateItem.isDeleted() ? -1 : entityManagerCtx);
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
        }

        // Clean the private
        entityManagerWrapper.remove(privateItem);
        LOGGER.debug("Done");
    }

    private void MarkObjectDeref(BaseObject item, int nextRevision) {
            ObjectDeref objectDeref = find(ObjectDeref.class, item.getKeyId().getObjId());
            if (objectDeref == null) {
                String msg = "Critical Error: Failed to find object reference to '" + item.getKeyId().getObjId() + "' of type '" + item.getClass().getSimpleName() + "'";
                LOGGER.error(msg);
                throw new RuntimeException(msg);
            }

            if (!item.isDeleted() && objectDeref.getKeyId().getEntityManagerCtx().equals(EntityManagerType.MAIN_ENTITY_MANAGER.id))
                return;

            ObjectDeref objectDerefDup = objectDeref.copy();
            objectDerefDup.getKeyId().setEntityManagerCtx(EntityManagerType.MAIN_ENTITY_MANAGER.id);
            if (item.isDeleted())
                objectDerefDup.getKeyId().setToRevision(nextRevision);
            objectDerefDup.setDeleted(item.isDeleted());
//            objectDerefDup.setEntityManagerCtx(EntityManagerType.MAIN_ENTITY_MANAGER.id);
            objectDerefDup.getKeyId().setEntityManagerCtx(EntityManagerType.MAIN_ENTITY_MANAGER.id);
//			LOGGER.debug("DBG " + objectDerefDup);
            entityManagerWrapper.persist(objectDerefDup);
            entityManagerWrapper.remove(objectDeref);
//			objectDeref = objectCrudSvc.readByClass(objectDeref.getKeyId().getObjId(), ObjectDeref.class);
//			LOGGER.debug("DBG " + objectDeref);

    }

    private <T extends BaseObject> void movePrivateToPublic(T privateItem, Class<T> clz, int nextRevision) {
        LOGGER.debug("Moving private to NON existing published object");
        T privateItemDup = (T) privateItem.copy();

        MarkObjectDeref(privateItem, nextRevision);

        // Building a new tip
        privateItemDup.setFromRevision(nextRevision);
        privateItemDup.getKeyId().setToRevision(Constants.TIP_REVISION);
        privateItemDup.getKeyId().setEntityManagerCtx(EntityManagerType.MAIN_ENTITY_MANAGER.id);
        entityManagerWrapper.persist(privateItemDup);

        // Clean private
        LOGGER.debug("Removing from private " + privateItem);
        entityManagerWrapper.remove(privateItem);

        LOGGER.debug("Object writing to public " + privateItemDup);

        // Test - that objects are find
        KeyId keyId = new KeyId();
        keyId.setObjId(privateItem.getKeyId().getObjId());
        keyId.setEntityManagerCtx(entityManagerCtx);
        keyId.setToRevision(Integer.MAX_VALUE);
        BaseObject obj;
        if ((obj = entityManagerWrapper.find(clz, keyId)) != null)
            throw new RuntimeException("Found unexpected private object: " + obj);
        keyId.setEntityManagerCtx(EntityManagerType.MAIN_ENTITY_MANAGER.id);
        if ((obj = entityManagerWrapper.find(clz, keyId)) == null)
            throw new RuntimeException("Failed to find public object");


        LOGGER.debug("Done");
    }


    private String buildGetPrivateQuery(Class<? extends BaseObject> objClass) {
		LOGGER.debug("Build query to get object of " + objClass.getSimpleName());
        return String.format("SELECT * FROM %s WHERE entityManagerCtx = %d", objClass.getSimpleName().toLowerCase(), entityManagerCtx);
	}

    private <T extends BaseObject> T getPublishedByPrivateObject(Class<T> clz, T item) {
        LOGGER.debug("Search for published object of " + item.getKeyId());
        KeyId key = item.getKeyId().copy();
		key.setEntityManagerCtx(EntityManagerType.MAIN_ENTITY_MANAGER.id);
//		LOGGER.debug("Search for publish object of key " + key);
        return entityManagerWrapper.find(clz, key);
    }
}
