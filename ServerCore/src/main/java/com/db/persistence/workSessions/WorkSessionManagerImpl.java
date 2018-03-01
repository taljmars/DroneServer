package com.db.persistence.workSessions;

import com.db.persistence.objectStore.EntityManagerBase;
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
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class WorkSessionManagerImpl implements WorkSessionManager {

    private final static Logger LOGGER = Logger.getLogger(WorkSessionManagerImpl.class);

    @Autowired
    private PersistencyManager persistencyManager;

    @Autowired
    private RevisionManager revisionManager;

    @Autowired
    private ApplicationContext applicationContext;

    private Map<String, WorkSession> workSessionMap;
    private Map<String, String> workSessionEntityMap;

    private WorkSession publicWorkSession;

    public WorkSessionManagerImpl() {
        workSessionMap = new HashMap<>();
        workSessionEntityMap = new HashMap<>();
    }

    @PostConstruct
    private void init() {
        EntityManagerBaseImpl entityManager = (EntityManagerBaseImpl) persistencyManager.createEntityManager(EntityManagerType.MAIN_ENTITY_MANAGER);
        publicWorkSession = applicationContext.getBean(WorkSession.class, "public", WorkSessionType.PUBLIC, entityManager.getId(), entityManager);
    }

    @Override
    @Transactional
    public WorkSession createSession(String userName) {
        if (workSessionMap.keySet().contains(userName)) {
            LOGGER.debug("User session found in cache");
            return workSessionMap.get(userName);
        }

        /*
            In worksession design, the main entity manager of the persistence manager will
            be the public session, while the virtualized ones will be called private
        */
        WorkSessionType type = userName.toLowerCase().equals("public") ? WorkSessionType.PUBLIC : WorkSessionType.PRIVATE;
        EntityManagerType entityManagerType = type == WorkSessionType.PUBLIC ? EntityManagerType.MAIN_ENTITY_MANAGER : EntityManagerType.VIRTUALIZED_ENTITY_MANAGER;
        EntityManagerBaseImpl entityManager = (EntityManagerBaseImpl) persistencyManager.createEntityManager(entityManagerType);

        LOGGER.debug("New session id was allocated: " + entityManager.getId());
        WorkSession session = applicationContext.getBean(WorkSession.class, userName, type, entityManager.getId(), entityManager);

        // Setting tenancy identified for object creation
//        KeyAspect.setTenantContext(session.getSessionId());

        /* Build an entity to represent the sesion in the database
         * Every object create under this session will be related to this object */
        WorkSessionEntity workSessionEntity = new WorkSessionEntity();
        workSessionEntity.getKeyId().setEntityManagerCtx(session.getSessionId());
//        workSessionEntity.setEntityManagerCtx(session.getSessionId());
        workSessionEntity.setReferredEntityManagerCtx(session.getSessionId());
        workSessionEntity.setUserName(session.getUserName());
        workSessionEntity.setDirty(false);

        if (type.equals(WorkSessionType.PUBLIC))
            workSessionEntity.getKeyId().setEntityManagerCtx(EntityManagerType.MAIN_ENTITY_MANAGER.id);

        workSessionEntity = publicWorkSession.update(workSessionEntity);

        workSessionMap.put(userName, session);
        workSessionEntityMap.put(userName, workSessionEntity.getKeyId().getObjId());
        return session;
    }

    @Override
    @Transactional
    public void destroySession(WorkSession workSession) {
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
        workSessionEntityMap.remove(workSession.getUserName());
        workSessionMap.remove(workSession.getUserName());

        LOGGER.debug("destroy entity manager");
        persistencyManager.destroyEntityManager(workSession.getEntityManager());

        publicWorkSession.flush();
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
        String workSessionEntryUuid = workSessionEntityMap.get(workSession.getUserName());
        return publicWorkSession.find(WorkSessionEntity.class, workSessionEntryUuid);
    }
}
