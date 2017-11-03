package com.db.persistence.workSessions;

import com.db.persistence.exception.DatabaseValidationException;
import com.db.persistence.exception.ObjectInstanceException;
import com.db.persistence.objectStore.PersistencyManager;
import com.db.persistence.objectStore.VirtualizedEntityManager;
import com.db.persistence.scheme.BaseObject;
import javassist.tools.rmi.ObjectNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.UUID;

//@Table
//@XmlAccessorType(XmlAccessType.PROPERTY)
//@Entity
@Scope("prototype")
@Component
public class WorkSession {

    private final static Logger logger = Logger.getLogger(WorkSession.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PersistencyManager persistencyManager;

    private String userName;
    //private EntityManager entityManager;
    VirtualizedEntityManager entityManager;
    private QueryExecutor queryExecutor;

    boolean permission = false;

    public WorkSession(String userName) {
        logger.debug("New WorkSession was allocation for user '" + userName + "'");
        this.userName = userName;
        if (!this.userName.equals("PUBLIC"))
            permission = true;
    }

    @PostConstruct
    protected void init() {
        this.entityManager = applicationContext.getBean(VirtualizedEntityManager.class, persistencyManager.createEntityManager(), permission);
        this.queryExecutor = new QueryExecutor(this.entityManager);
//        workSessionCache = new WorkSessionPrivateCache();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

//    @Transactional
//    public <T extends BaseObject> EntityManager getEntityManager() {
//        return entityManager;
//    }

    public <T extends BaseObject> VirtualizedEntityManager getEntityManager() {
        return entityManager;
    }

    @Transactional
    public QueryExecutor getQueryExecutor() {
        return queryExecutor;
    }


    // Basic CRUD operation
    @Transactional
    public <T extends BaseObject> T find(Class<T> clz, UUID uuid) {
        logger.debug("In work session, find " + clz + " ,key=" + uuid);
        T res = entityManager.find(clz, uuid);
        return res;
    }

    @Transactional
    public void flush() {
        entityManager.flush();
    }

    @Transactional
    public <T extends BaseObject> T delete(T object) {
        return entityManager.delete(object);
    }

//    @Transactional
//    public <T extends BaseObject> void persist(T object) {
//        entityManager.persist(object);
//    }

    @Transactional
    public <T extends BaseObject> T update(T object) {
        return getEntityManager().update(object);
    }

    @Transactional
    public <T extends BaseObject> T pull(T obj) {
        return entityManager.pull(obj);
    }

    @Transactional
    public void discard() {
        entityManager.discard();
//        workSession.flush();
    }

    @Transactional
    public void publish(int revisionTag) {
        entityManager.publish(revisionTag);
    }

    public BaseObject find(UUID uid) {
        return entityManager.find(uid);
    }
}
