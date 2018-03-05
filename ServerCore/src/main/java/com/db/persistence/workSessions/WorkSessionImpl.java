package com.db.persistence.workSessions;

import com.db.persistence.objectStore.EntityManagerBase;
import com.db.persistence.objectStore.EntityManagerBaseImpl;
import com.db.persistence.scheme.BaseObject;
import com.db.persistence.workSession.QueryExecutor;
import com.db.persistence.workSession.WorkSession;
import com.db.persistence.workSession.WorkSessionManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Scope("prototype")
@Component
public class WorkSessionImpl implements WorkSession {

    private final static Logger LOGGER = Logger.getLogger(WorkSessionImpl.class);

    @Autowired
    private ApplicationContext applicationContext;

    private WorkSessionManager workSessionManager;

    private String token;
    private String userName1;
    private int sessionId;
    private WorkSessionType type;
    private Boolean isDirty;
    private EntityManagerBaseImpl entityManager;
    private QueryExecutor queryExecutor;

    public WorkSessionImpl(String token, String userName, WorkSessionType type, Integer sessionId, EntityManagerBaseImpl entityManager) {
        LOGGER.debug("New WorkSession was allocation for token '" + token + "', user '" + userName + "'");
        this.token = token;
        this.userName1 = userName;
        this.type = type;
        this.sessionId = sessionId;
        this.entityManager = entityManager;
        this.isDirty = false;
    }

    public WorkSessionImpl(WorkSessionImpl workSession) {
        this.token = workSession.token;
        this.userName1 = workSession.userName1;
        this.type = workSession.type;
        this.sessionId = workSession.sessionId;
        this.entityManager = workSession.entityManager;
        this.isDirty = workSession.isDirty;
    }

    @PostConstruct
    protected void init() {
        this.queryExecutor = applicationContext.getBean(QueryExecutor.class, this);
//        workSessionCache = new WorkSessionPrivateCache();
        this.workSessionManager = applicationContext.getBean(WorkSessionManager.class);
    }

    @Override
    public <T extends BaseObject> EntityManagerBase getEntityManager() {
        return entityManager;
    }

    @Override
    @Transactional
    public QueryExecutor getQueryExecutor() {
        return queryExecutor;
    }

    // Basic CRUD operation
    @Override
    @Transactional
    public <T extends BaseObject> T find(Class<T> clz, String uuid) {
        LOGGER.debug("In work session, find " + clz + " ,key=" + uuid);
        T res = entityManager.find(clz, uuid);
        return res;
    }

    @Override
    @Transactional
    public void flush() {
        entityManager.flush();
    }

    @Override
    @Transactional
    public <T extends BaseObject> T delete(T object) {
        T res = entityManager.delete(object);
        isDirty = isDirty || workSessionManager.markDirty(this, object);
        return res;
    }

    @Override
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

    @Override
    public BaseObject find(String uid) {
        return entityManager.find(uid);
    }

    @Override
    public WorkSession clone() {
        return applicationContext.getBean(WorkSession.class, this);
    }

    @Override
    @Transactional
    public WorkSession publish() {
        LOGGER.debug("Publishing db session id '" + sessionId + "' of token '" + token + "' of user '" + userName1 + "'");
        entityManager.publish();
        return workSessionManager.destroySession(this);
    }

    @Override
    @Transactional
    public WorkSession discard() {
        LOGGER.debug("Discarding session id '" + sessionId + "' of token '" + token + "' of user '" + userName1 + "'");
        entityManager.discard();
        return workSessionManager.destroySession(this);
    }

    @Override
    public int getSessionId() {
        return sessionId;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getUserName1() {
        return userName1;
    }
}
