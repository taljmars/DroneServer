package com.db.persistence.services.internal;

import com.db.persistence.objectStore.PersistencyManager;
import com.db.persistence.objectStore.VirtualizedEntityManager;
import com.db.persistence.workSessions.WorkSession;
import com.db.persistence.workSessions.WorkSessionManager;
import com.db.persistence.scheme.Revision;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by taljmars on 4/29/17.
 */
@Lazy
@Component
public class RevisionManager {

    final static Logger logger = Logger.getLogger(RevisionManager.class);

    //	@PersistenceContext
//	@Autowired
    WorkSession workSession;

    @Autowired
    private WorkSessionManager workSessionManager;

    @Autowired
    PersistencyManager persistencyManager;

    EntityManager entityManager;

    @PostConstruct
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void init() {
        setForUser("PUBLIC");
    }

    String currentUserName = "";
    @Transactional
    public void setForUser(String userName) {
        if (currentUserName.equals(userName))
            return;

        logger.debug("Context was changed for user : " + userName);
//        workSession = workSessionManager.createSession(userName);
        //this.entityManager = objectStoreSession.getEntityManager();
        this.entityManager = persistencyManager.createEntityManager();
    }

    private Revision initializeRevision() {
        Revision revision = new Revision();
        revision.setCurrentRevision(0);
        revision.getKeyId().setPrivatelyModified(false);
        revision.getKeyId().setToRevision(0);
        logger.debug("First time creating revision " + revision);
        entityManager.persist(revision);
        entityManager.flush();
        revision = entityManager.find(Revision.class, revision.getKeyId());
        return revision;
    }

    private Revision getRevisionObject() {
        Query query = entityManager.createNativeQuery("SELECT * FROM revision", Revision.class);
        List<Revision> list = query.getResultList();
        if (list.size() != 0)
            return list.get(0);

        return initializeRevision();
    }

    @Transactional
    public int getNextRevision() {
        return getRevisionObject().getCurrentRevision() + 1;
    }

    @Transactional
    public void advance() {
        Revision revision = getRevisionObject();
        revision.setCurrentRevision(getNextRevision());
        entityManager.flush();
    }
}
