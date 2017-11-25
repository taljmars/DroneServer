package com.db.persistence.objectStore;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Component
public class PersistencyManager {

    private static final Logger logger = Logger.getLogger(PersistencyManager.class);
    private static final Integer ENTITY_MANAGER_AMOUNT = 512;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private ApplicationContext applicationContext;

    private Boolean[] entityManagersIds;
    private static final Boolean ALLOCATED = true;
    private static final Boolean FREE = false;

    public PersistencyManager() {
        entityManagersIds = new Boolean[ENTITY_MANAGER_AMOUNT];
        for (int i = 0; i < ENTITY_MANAGER_AMOUNT; i++)
            entityManagersIds[i] = FREE;

        entityManagersIds[EntityManagerType.MAIN_ENTITY_MANAGER.id] = ALLOCATED;
    }

    @PostConstruct
    private void init() {
    }

    public EntityManager createEntityManager() {
        logger.debug("New Entity Manager was created");
        return SharedEntityManagerCreator.createSharedEntityManager(entityManagerFactory);
    }

//    @Transactional(propagation = Propagation.MANDATORY)
    @Transactional(propagation = Propagation.REQUIRED)
    public EntityManagerBase createEntityManager(EntityManagerType type) {
        EntityManagerBase entityManager = null;
        if (type.equals(EntityManagerType.MAIN_ENTITY_MANAGER)) {
            entityManager = applicationContext.getBean(NonVirtualizedEntityManager.class, createEntityManager());
        }
        else {
            Integer entityManagerCtx = allocateId(type.id);
            entityManager = applicationContext.getBean(VirtualizedEntityManager.class, createEntityManager(), entityManagerCtx);
        }

        return entityManager;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void destroyEntityManager(EntityManagerBase entityManager) {
        if (entityManager instanceof NonVirtualizedEntityManager) {
            logger.debug("Free main entity manager");
            deallocateId(EntityManagerType.MAIN_ENTITY_MANAGER.id);
        }
        else {
            Integer id = ((VirtualizedEntityManager) entityManager).entityManagerCtx;
            logger.debug("Free entity manager id: " + id);
            deallocateId(id);
        }
    }

    private synchronized Integer allocateId(Integer id) {
        if (id == EntityManagerType.MAIN_ENTITY_MANAGER.id)
            return EntityManagerType.MAIN_ENTITY_MANAGER.id;

        for (int i = 0; i < ENTITY_MANAGER_AMOUNT; i++) {
            if (entityManagersIds[i] == FREE) {
                entityManagersIds[i] = ALLOCATED;
                return i;
            }
        }
        return EntityManagerType.UNKNOWN_ENTITY_MANAGER.id;
    }

    private synchronized void deallocateId(Integer id) {
        if (id == EntityManagerType.MAIN_ENTITY_MANAGER.id)
            return;
        entityManagersIds[id] = FREE;
    }
}
