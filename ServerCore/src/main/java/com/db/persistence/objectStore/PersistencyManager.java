package com.db.persistence.objectStore;

import com.db.persistence.workSessions.WorkSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Component
public class PersistencyManager {

    final static Logger logger = Logger.getLogger(PersistencyManager.class);

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private ApplicationContext applicationContext;

    public EntityManager createEntityManager() {
        logger.debug("New Entity Manager was created");
        return SharedEntityManagerCreator.createSharedEntityManager(entityManagerFactory);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public WorkSession createWorkSession(String userName) {
        WorkSession session = applicationContext.getBean(WorkSession.class, userName);
        return session;
    }
}
