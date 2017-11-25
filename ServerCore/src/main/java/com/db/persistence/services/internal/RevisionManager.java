package com.db.persistence.services.internal;

import com.db.persistence.objectStore.PersistencyManager;
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

    private final static Logger logger = Logger.getLogger(RevisionManager.class);

    @Autowired
    private PersistencyManager persistencyManager;

    private EntityManager entityManager;

    @PostConstruct
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void init() {
        logger.debug("Initialize revision manager");
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
    public synchronized void advance() {
        Revision revision = getRevisionObject();
        revision.setCurrentRevision(getNextRevision());
        entityManager.flush();
    }
}
