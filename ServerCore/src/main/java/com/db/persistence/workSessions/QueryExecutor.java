package com.db.persistence.workSessions;

import com.db.persistence.scheme.BaseObject;
import org.apache.log4j.Logger;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class QueryExecutor {

    private final static Logger LOGGER = Logger.getLogger(QueryExecutor.class);

    private WorkSession workSession;

    public QueryExecutor(WorkSession workSession) {
        this.workSession = workSession;
    }

    public <T extends BaseObject> List<T> createNativeQuery(String queryString, Class<T> clz) {
        LOGGER.debug("Query Executor request: " + queryString + ", for class: " + clz.getSimpleName());
        Query query = workSession.getEntityManager().createNativeQuery(queryString, clz);
        List<T> res = query.getResultList();
        return translateResults(res);
    }

    public <T extends BaseObject> List<T> createQuery(String queryString) {
        LOGGER.debug("Query Executor request: " + queryString);
        Query query = workSession.getEntityManager().createQuery(queryString);
        List<T> res = query.getResultList();
        return translateResults(res);
    }

    public <T extends BaseObject> List<T> createNamedQuery(String queryString, Class<T> clz) {
        LOGGER.debug("Query Executor request: " + queryString + ", for class: " + clz.getSimpleName());
        TypedQuery<T> query = workSession.getEntityManager().createNamedQuery(queryString, clz);
        List<T> res = query.getResultList();
        return translateResults(res);
    }

    private <T extends BaseObject> List<T> translateResults(List<T> res) {
        LOGGER.debug("Going to translate " + res.size() + " objects");
        List<T> finalResults = new ArrayList<>();
        for (T obj : res) {
            T updatedObject = workSession.getEntityManager().pull(obj);
            if (updatedObject != null)
                finalResults.add(updatedObject);
        }
        return finalResults;
    }
}
