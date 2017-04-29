package com.dronedb.persistence.services.internal;

import com.dronedb.persistence.scheme.Revision;
import com.dronedb.persistence.services.QueryRequest;
import com.dronedb.persistence.services.QuerySvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by oem on 4/29/17.
 */
@Lazy
@Component
public class RevisionManager {

    @PersistenceContext
    private EntityManager entityManager;

    private Revision initializeRevision() {
        Revision revision = new Revision();
        revision.setCurrentRevision(0);
        revision.getKeyId().setPrivatelyModified(false);
        revision.getKeyId().setToRevision(0);
        System.out.println("First time creating revision " + revision);
        entityManager.persist(revision);
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

    public int getNextRevision() {
        return getRevisionObject().getCurrentRevision() + 1;
    }

    public void advance() {
        Revision revision = getRevisionObject();
//        Revision revisionDup = revision.copy();
//        revisionDup.getKeyId().setToRevision();
        revision.setCurrentRevision(getNextRevision());
//        revision.

        entityManager.flush();
    }
}
