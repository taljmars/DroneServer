/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.persistence.workSessions;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.workSession.QueryExecutor;
import com.db.persistence.workSession.WorkSession;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.db.server.SpringProfiles.EclipseLink;

@Component
@Profile(EclipseLink)
@Scope("prototype")
public class QueryExecutorEclipseLink implements QueryExecutor {

    private final static Logger LOGGER = Logger.getLogger(QueryExecutorEclipseLink.class);

    private WorkSession workSession;

    public QueryExecutorEclipseLink(WorkSession workSession) {
        this.workSession = workSession;
    }

    @Override
    public <T extends BaseObject> List<T> createNativeQuery(String queryString, Class<T> clz) {
        LOGGER.debug("Query Executor request: " + queryString + ", for class: " + clz.getSimpleName());
        Query query = workSession.getEntityManager().createNativeQuery(queryString, clz);
        List<T> res = query.getResultList();
        return translateResults(res);
    }

    @Override
    public <T extends BaseObject> List<T> createNativeQuery(String queryString) {
        LOGGER.debug("Query Executor request: " + queryString);
        Query query = workSession.getEntityManager().createQuery(queryString);
        List<T> res = query.getResultList();
        return translateResults(res);
    }

    @Override
    public <T extends BaseObject> List<T> createNamedQuery(String queryString, Class<T> clz, int offset, int limit) {
        LOGGER.debug("Query Executor request: " + queryString + ", for class: " + clz.getSimpleName());
        TypedQuery<T> query = workSession.getEntityManager().createNamedQuery(queryString, clz);
//        query.setHint("eclipselink.jdbc.parameter-delimiter", ":");
//        query.setHint("eclipselink.jdbc.native-sql", "true");
        query.setHint("eclipselink.jdbc.bind-parameters", "false");
//        query.setHint("jdbc.parameter-delimiter", ":");
        if (query.getParameters().stream().filter(e -> e.getName().equals("CTX")).count() != 0)
            return createNamedQuery(queryString, new HashMap<>(), clz, offset, limit);

        if (offset != 0 || limit != 0) {
            query.setFirstResult(offset);
            query.setMaxResults(limit);
        }

        query.setParameter(1, "1");
        query.setParameter(2, "1");
        List<T> res = query.getResultList();
        return translateResults(res);
    }

    @Override
    public <T extends BaseObject> List<T> createNamedQuery(String queryString, Map<String, Object> parameterSet, Class<T> clz, int offset, int limit) {
        LOGGER.debug("Query Executor request: " + queryString + ", for class: " + clz.getSimpleName());
        TypedQuery<T> query = workSession.getEntityManager().createNamedQuery(queryString, clz);
        if (parameterSet != null) {
            for (Map.Entry<String, Object> p : parameterSet.entrySet())
                query.setParameter(p.getKey(), p.getValue());
        }

        if (query.getParameters().stream().anyMatch((p) -> p.getName().equals("CTX")))
            query.setParameter("CTX", workSession.getSessionId());

        if (offset != 0 || limit != 0) {
            query.setFirstResult(offset);
            query.setMaxResults(limit);
        }

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

    @Override
    public WorkSession getWorkSession() {
        return workSession;
    }
}
