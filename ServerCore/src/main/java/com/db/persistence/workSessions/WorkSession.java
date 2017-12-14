package com.db.persistence.workSessions;

import com.db.persistence.objectStore.EntityManagerBase;
import com.db.persistence.scheme.BaseObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Scope("prototype")
@Component
public class WorkSession {

    private final static Logger LOGGER = Logger.getLogger(WorkSession.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private WorkSessionManager workSessionManager;

    private String userName;
    private int sessionId;
    private WorkSessionType type;
    private Boolean isDirty;
    private EntityManagerBase entityManager;
    private QueryExecutor queryExecutor;

    public WorkSession(String userName, WorkSessionType type, Integer sessionId, EntityManagerBase entityManager) {
        LOGGER.debug("New WorkSession was allocation for user '" + userName + "'");
        this.userName = userName;
        this.type = type;
        this.sessionId = sessionId;
        this.entityManager = entityManager;
        this.isDirty = false;
    }

    public WorkSession(WorkSession workSession) {
        this.userName = workSession.userName;
        this.type = workSession.type;
        this.sessionId = workSession.sessionId;
        this.entityManager = workSession.entityManager;
        this.isDirty = workSession.isDirty;
    }

    @PostConstruct
    protected void init() {
        this.queryExecutor = new QueryExecutor(this);
//        workSessionCache = new WorkSessionPrivateCache();
    }

    public <T extends BaseObject> EntityManagerBase getEntityManager() {
        return entityManager;
    }

    @Transactional
    public QueryExecutor getQueryExecutor() {
        return queryExecutor;
    }

    // Basic CRUD operation
    @Transactional
    public <T extends BaseObject> T find(Class<T> clz, UUID uuid) {
        LOGGER.debug("In work session, find " + clz + " ,key=" + uuid);
        T res = entityManager.find(clz, uuid);
        return res;
    }

    @Transactional
    public void flush() {
        entityManager.flush();
    }

    @Transactional
    public <T extends BaseObject> T delete(T object) {
        T res = entityManager.delete(object);
        isDirty = isDirty || workSessionManager.markDirty(this, object);
        return res;
    }

    @Transactional
    public <T extends BaseObject> T update(T object) {
        T res = getEntityManager().update(object);
        isDirty = isDirty || workSessionManager.markDirty(this, object);
        return res;
    }

    @Transactional
    public <T extends BaseObject> T pull(T obj) {
        return entityManager.pull(obj);
    }

    public BaseObject find(UUID uid) {
        return entityManager.find(uid);
    }

    @Override
    public WorkSession clone() {
        return applicationContext.getBean(WorkSession.class, this);
    }

    @Transactional
    public void publish() {
        LOGGER.debug("Publishing session id '" + sessionId + "' of user '" + userName + "'");
        entityManager.publish();
        workSessionManager.destroySession(this);
    }

    @Transactional
    public void discard() {
        LOGGER.debug("Discarding session id '" + sessionId + "' of user '" + userName + "'");
        entityManager.discard();
        workSessionManager.destroySession(this);
//        workSessionManager.destroySession(this, true);
    }

    public int getSessionId() {
        return sessionId;
    }

    public String getUserName() {
        return userName;
    }
}
