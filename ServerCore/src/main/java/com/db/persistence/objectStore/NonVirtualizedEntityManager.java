package com.db.persistence.objectStore;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.Constants;
import com.db.persistence.scheme.KeyId;
import com.db.persistence.services.internal.RevisionManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

@Lazy
@Component
public class NonVirtualizedEntityManager extends EntityManagerBaseImpl {

    private final static Logger LOGGER = Logger.getLogger(NonVirtualizedEntityManager.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    public void setRevisionManager(RevisionManager revisionManager) {
        this.revisionManager = revisionManager;
    }

    private EntityManager entityManager;

    public <T extends BaseObject> NonVirtualizedEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostConstruct
    public void init() {
        LOGGER.debug("Initialize Non VEM");
        this.entityManagerWrapper = applicationContext.getBean(SimpleEntityManagerWrapper.class, entityManager, EntityManagerType.MAIN_ENTITY_MANAGER.id);
    }

    @Override
    public <T extends BaseObject> T find(Class<T> clz, String uuid) {
        LOGGER.debug("Searching for " + clz.getSimpleName() + " ,uid=" + uuid);
        KeyId keyId = new KeyId();
        keyId.setToRevision(Integer.MAX_VALUE);
        keyId.setObjId(uuid);
        keyId.setEntityManagerCtx(EntityManagerType.MAIN_ENTITY_MANAGER.id);
        return entityManagerWrapper.find(clz, keyId);
    }

    @Override
    public <T extends BaseObject> T delete(T object) {
        LOGGER.debug("Removing: " + object);

        // We first start by getting the deletion candidate from the public
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

        DeleteObjectDeref(existingObject);
        entityManagerWrapper.remove(existingObject);
        return existingObject;
    }

    private  <T extends BaseObject> void persist(T object) {
        LOGGER.debug("Persist object: " + object);
        object.getKeyId().setToRevision(Constants.TIP_REVISION);
        object.getKeyId().setEntityManagerCtx(EntityManagerType.MAIN_ENTITY_MANAGER.id);
        object.setDeleted(false); //TODO: Check if we need this one, wasn't tested at all

        entityManagerWrapper.persist(object);
    }

    public <T extends BaseObject> T update(T object) {
        LOGGER.debug("VEM UPDATE called " + object);

        T existingObject = find((Class<T>) object.getClass(), object.getKeyId().getObjId());

        // Handling a case were the object doesn't exist in the publish db.
        // This is the creation time of this object
        if (existingObject == null) {
            // Nothing exist at all, creating it for the first time directly on the public db
            LOGGER.debug("Object will be written to the database for the first time, validating object");

            // Setting toVersion field to represent the last version
            object.getKeyId().setToRevision(Constants.TIP_REVISION);
            object.getKeyId().setEntityManagerCtx(EntityManagerType.MAIN_ENTITY_MANAGER.id);
            object.setDeleted(false); //TODO: Check if we need this one, wasn't tested at all
            entityManagerWrapper.persist(object);
            existingObject = object;

            // Update ObjectDeref table for future search
            CreateObjectDeref(object);
        }

        // Handling a case where we've found an object in the public session.
        else {
            // found in public, updating the object
            existingObject.set(object);
        }

        LOGGER.debug("Updated " + existingObject);
        return existingObject;
    }

    @Override
    public void discard() {
//        virtualizedEntityManager.discard();
        //TODO: BUG ?!?!?!?
    }

    @Override
    public void publish() {
        int nextRevision = revisionManager.getNextRevision();

        LOGGER.debug("Flush Entity Manager");
        entityManagerWrapper.flush();

        LOGGER.debug("Set next revision");
        revisionManager.advance();

//        virtualizedEntityManager.publish();
    }

    @Override
    public Integer getId() {
        return EntityManagerType.MAIN_ENTITY_MANAGER.id;
    }

    /*****************************************************************************/
    /*****************************************************************************/
    /*****************************************************************************/
    /*****************************************************************************/

    @Override
    public SimpleEntityManagerWrapper getEntityManager() {
        return entityManagerWrapper;
//        return virtualizedEntityManager.entityManagerWrapper;
    }
}
