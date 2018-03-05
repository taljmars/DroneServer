package com.db.persistence.workSession;

import com.db.persistence.objectStore.EntityManagerBase;
import com.db.persistence.scheme.BaseObject;

public interface WorkSession {

    <T extends BaseObject> EntityManagerBase getEntityManager();

    QueryExecutor getQueryExecutor();

    <T extends BaseObject> T find(Class<T> clz, String uuid);

    void flush();

    <T extends BaseObject> T delete(T object);

    <T extends BaseObject> T update(T object);

    BaseObject find(String uid);

    WorkSession publish();

    WorkSession discard();

    int getSessionId();

    String getToken();

    void setToken(String token);

    String getUserName1();
}
