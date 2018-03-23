package com.db.persistence.workSessions;

import com.db.persistence.objectStore.EntityManagerBaseImpl;
import com.db.persistence.objectStore.EntityManagerType;
import com.db.persistence.objectStore.PersistencyManager;
import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.WorkSessionEntity;
import com.db.persistence.services.internal.RevisionManager;
import com.db.persistence.workSession.WorkSession;
import com.db.persistence.workSession.WorkSessionManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static com.db.persistence.workSession.Constant.INTERNAL_SERVER_USER_NAME;
import static com.db.persistence.workSession.Constant.INTERNAL_SERVER_USER_TOKEN;

@Component
public class WorkSessionManagerImpl implements WorkSessionManager {

    private final static Logger LOGGER = Logger.getLogger(WorkSessionManagerImpl.class);

    @Autowired
    private PersistencyManager persistencyManager;

    @Autowired
    private RevisionManager revisionManager;

    @Autowired
    private ApplicationContext applicationContext;

    private Map<String  /*Security Session Token*/              , WorkSession> workSessionMap;
    private Map<Integer /*DB Session ctx*/                      , String /* WorkSessionEntityId*/> workSessionEntityMap;
    private Map<Integer /*DB Session ctx*/                      , String /* User Name*/> workSessionUserNames;
    private Map<String  /*UserName - when there is not session*/, WorkSession> orphansWorkSessionMap;

    private WorkSession publicWorkSession;

    public WorkSessionManagerImpl() {
        workSessionMap = new HashMap<>();
        workSessionEntityMap = new HashMap<>();
        orphansWorkSessionMap = new HashMap<>();
        workSessionUserNames = new HashMap<>();
    }

    @PostConstruct
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void init() {
//        EntityManagerBaseImpl entityManager = (EntityManagerBaseImpl) persistencyManager.createEntityManager(EntityManagerType.MAIN_ENTITY_MANAGER);
//        publicWorkSession = applicationContext.getBean(WorkSession.class, INTERNAL_SERVER_USER_TOKEN, INTERNAL_SERVER_USER_NAME, WorkSessionType.PUBLIC, entityManager.getId(), entityManager);
        publicWorkSession = createSession(INTERNAL_SERVER_USER_TOKEN, INTERNAL_SERVER_USER_NAME);
    }

    @Override
    @Transactional
    public WorkSession getSessionByToken(String token) {
        if (workSessionMap.keySet().contains(token)) {
            LOGGER.debug("User session found in cache");
            return workSessionMap.get(token);
        }

        //TODO: have a nicer message
//        LOGGER.debug("Session doesn't exist");
        return null;
    }

    @Override
    @Transactional
    public WorkSession getOrhpanSessionByUserName(String user) {
        if (orphansWorkSessionMap.keySet().contains(user)) {
            LOGGER.debug("User session found in cache");
            return orphansWorkSessionMap.get(user);
        }

        //TODO: have a nicer message
        LOGGER.debug("Session doesn't exist");
        return null;
    }

    @Override
    public WorkSession reviveSession(WorkSession workSession, String token) {
        WorkSession workSession1 = orphansWorkSessionMap.remove(workSession.getUserName1());
        workSession1.setToken(token);
        workSessionMap.put(token, workSession1);
        return workSession1;
    }

    @Override
    public WorkSession orphanizeSession(String token) {
        WorkSession workSessionToOrphanize = workSessionMap.remove(token);
        if (workSessionToOrphanize.isDirty()) {
            LOGGER.debug("Session is dirty - need to be orphanize");
            orphansWorkSessionMap.put(workSessionToOrphanize.getUserName1(), workSessionToOrphanize);
        }
        else {
            LOGGER.debug("Session is clean, skip orphanization");
        }
        return workSessionToOrphanize;
    }

    @Override
    @Transactional
    public WorkSession createSession(String token, String userName1) {
        if (workSessionMap.keySet().contains(token)) {
            LOGGER.debug("User session found in cache");
//            return workSessionMap.get(userName);
            throw new RuntimeException("Session aleady exist");
        }

        /*
            In worksession design, the main entity manager of the persistence manager will
            be the public session, while the virtualized ones will be called private
        */
        WorkSessionType type = token.equals(INTERNAL_SERVER_USER_TOKEN) ? WorkSessionType.PUBLIC : WorkSessionType.PRIVATE;
        EntityManagerType entityManagerType = type == WorkSessionType.PUBLIC ? EntityManagerType.MAIN_ENTITY_MANAGER : EntityManagerType.VIRTUALIZED_ENTITY_MANAGER;
        EntityManagerBaseImpl entityManager = (EntityManagerBaseImpl) persistencyManager.createEntityManager(entityManagerType);

        LOGGER.debug("New session id was allocated: " + entityManager.getId());
        WorkSession workSession = applicationContext.getBean(WorkSession.class, token, userName1, type, entityManager.getId(), entityManager);

        // Setting tenancy identified for object creation
//        KeyAspect.setTenantContext(session.getSessionId());

        /* Build an entity to represent the sesion in the database
         * Every object create under this session will be related to this object */
        WorkSessionEntity workSessionEntity = new WorkSessionEntity();
        workSessionEntity.getKeyId().setEntityManagerCtx(workSession.getSessionId());
//        workSessionEntity.setEntityManagerCtx(session.getSessionId());
        workSessionEntity.setReferredEntityManagerCtx(workSession.getSessionId());
        workSessionEntity.setUserName(workSession.getUserName1());
        workSessionEntity.setDirty(false);

        if (type.equals(WorkSessionType.PUBLIC)) {
            workSessionEntity.getKeyId().setEntityManagerCtx(EntityManagerType.MAIN_ENTITY_MANAGER.id);
            workSessionEntity = workSession.update(workSessionEntity);
        }
        else {
            workSessionEntity = publicWorkSession.update(workSessionEntity);
        }


        workSessionMap.put(token, workSession);
        workSessionEntityMap.put(workSession.getSessionId(), workSessionEntity.getKeyId().getObjId());
        workSessionUserNames.put(workSession.getSessionId(), workSession.getUserName1());
        return workSession;
    }

    @Override
    @Transactional
    public WorkSession destroySession(WorkSession workSession) {
        LOGGER.debug("Destroying session '" + workSession.getSessionId() + "'");
        WorkSessionEntity workSessionEntity = getWorkSessionEntity(workSession);
        if (workSessionEntity != null) {
            LOGGER.debug("Found entity object for deletion: " + workSessionEntity);

            publicWorkSession.delete(workSessionEntity);
        }
        else {
            throw new RuntimeException("Failed to find entity manager");
        }

        LOGGER.debug("Flush entity manager");
        workSession.flush();

        LOGGER.debug("Remove Entity manager from cache");
//        workSessionEntityMap.remove(workSession.getUserName1());
        workSessionEntityMap.remove(workSession.getSessionId());
        String userName = workSessionUserNames.remove(workSession.getSessionId());
        if (orphansWorkSessionMap.containsKey(userName))
            orphansWorkSessionMap.remove(userName);
        else
            workSessionMap.remove(workSession.getToken());

        LOGGER.debug("destroy entity manager");
        persistencyManager.destroyEntityManager(workSession.getEntityManager());

        publicWorkSession.flush();

        return workSession;
    }

    @Override
    public <T extends BaseObject> Boolean markDirty(WorkSession workSession, T obj) {
        if (obj instanceof WorkSessionEntity)
            return false;

        WorkSessionEntity workSessionEntity = getWorkSessionEntity(workSession);
        if (workSessionEntity == null)
            throw new RuntimeException("Failed to find worksession");

        workSessionEntity.setDirty(true);
        workSessionEntity = publicWorkSession.update(workSessionEntity);
        return true;
    }

    private WorkSessionEntity getWorkSessionEntity(WorkSession workSession) {
//        String workSessionEntryUuid = workSessionEntityMap.get(workSession.getUserName1());
        String workSessionEntryUuid = workSessionEntityMap.get(workSession.getSessionId());
        return publicWorkSession.find(WorkSessionEntity.class, workSessionEntryUuid);
    }

    @Override
    public String getUserNameByCtx(Integer ctx) {
        String userName = workSessionUserNames.get(ctx);
        return userName;
    }
}
