package com.db.persistence.workSessions;

import com.db.persistence.objectStore.EntityManagerType;
import com.db.persistence.objectStore.PersistencyManager;
import com.db.persistence.objectStore.EntityManagerBase;
import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.WorkSessionEntity;
import com.db.persistence.services.internal.RevisionManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class WorkSessionManager {

    private final static Logger logger = Logger.getLogger(WorkSessionManager.class);

    @Autowired
    private PersistencyManager persistencyManager;

    @Autowired
    private RevisionManager revisionManager;

    @Autowired
    private ApplicationContext applicationContext;

    private Map<String, WorkSession> workSessionMap;
    private Map<String, UUID> workSessionEntityMap;

    private WorkSession publicWorkSession;

    public WorkSessionManager() {
        workSessionMap = new HashMap<>();
        workSessionEntityMap = new HashMap<>();
    }

    @PostConstruct
    private void init() {
        EntityManagerBase entityManager = persistencyManager.createEntityManager(EntityManagerType.MAIN_ENTITY_MANAGER);
        publicWorkSession = applicationContext.getBean(WorkSession.class, "public", WorkSessionType.PUBLIC, entityManager.getId(), entityManager);
    }

    @Transactional
    public WorkSession createSession(String userName) {
        if (workSessionMap.keySet().contains(userName))
            return workSessionMap.get(userName);

        /*
            In worksession design, the main entity manager of the persistence manager will
            be the public session, while the virtualized ones will be called private
        */
        WorkSessionType type = userName.toLowerCase().equals("public") ? WorkSessionType.PUBLIC : WorkSessionType.PRIVATE;
        EntityManagerType entityManagerType = type == WorkSessionType.PUBLIC ? EntityManagerType.MAIN_ENTITY_MANAGER : EntityManagerType.VIRTUALIZED_ENTITY_MANAGER;
        EntityManagerBase entityManager = persistencyManager.createEntityManager(entityManagerType);

        logger.debug("New session id was allocated: " + entityManager.getId());
        WorkSession session = applicationContext.getBean(WorkSession.class, userName, type, entityManager.getId(), entityManager);

        /* Build an entity to represent the sesion in the database
         * Every object create under this session will be related to this object */
        WorkSessionEntity workSessionEntity = new WorkSessionEntity();
        workSessionEntity.setEntityManagerCtx(session.getSessionId());
        workSessionEntity.setReferredEntityManagerCtx(session.getSessionId());
        workSessionEntity.setUserName(session.getUserName());
        workSessionEntity.setDirty(false);

        if (type.equals(WorkSessionType.PUBLIC))
            workSessionEntity.getKeyId().setPrivatelyModified(false);

        workSessionEntity = publicWorkSession.update(workSessionEntity);

        workSessionMap.put(userName, session);
        workSessionEntityMap.put(userName, workSessionEntity.getKeyId().getObjId());
        return session;
    }

    @Transactional
    public void destroySession(WorkSession workSession) {
        logger.debug("Destroying session '" + workSession.getSessionId());
        WorkSessionEntity workSessionEntity = getWorkSessionEntity(workSession);
        if (workSessionEntity != null) {
            logger.debug("Found entity object for deletion: " + workSessionEntity);

            publicWorkSession.delete(workSessionEntity);
        }
        else {
            throw new RuntimeException("Failed to find entity manager");
        }

        persistencyManager.destroyEntityManager(workSession.getEntityManager());
        workSessionEntityMap.remove(workSession.getUserName());
        workSessionMap.remove(workSession.getUserName());
        publicWorkSession.flush();
    }

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
        UUID workSessionEntryUuid = workSessionEntityMap.get(workSession.getUserName());
        return publicWorkSession.find(WorkSessionEntity.class, workSessionEntryUuid);
    }
}
