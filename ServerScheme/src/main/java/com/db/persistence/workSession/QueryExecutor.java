package com.db.persistence.workSession;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.workSession.WorkSession;

import java.util.List;
import java.util.Map;

public interface QueryExecutor {

    <T extends BaseObject> List<T> createNativeQuery(String queryString, Class<T> clz);

    <T extends BaseObject> List<T> createNativeQuery(String queryString);

    <T extends BaseObject> List<T> createNamedQuery(String queryString, Class<T> clz);

    <T extends BaseObject> List<T> createNamedQuery(String queryString, Map<String, Object> parameterSet, Class<T> clz);

    WorkSession getWorkSession();

}
