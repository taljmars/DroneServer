package com.db.persistence.workSession;

import com.db.persistence.objectStore.EntityManagerBase;
import com.db.persistence.scheme.BaseObject;

public interface WorkSession<TOKEN> {

    EntityManagerBase getEntityManager();

    QueryExecutor getQueryExecutor();

    <T extends BaseObject> T find(Class<T> clz, String uuid);

    void flush();

    <T extends BaseObject> T delete(T object);

    <T extends BaseObject> T update(T object);

    <T extends BaseObject> T find(String uid);

    WorkSession publish();

    WorkSession discard();

    int getSessionId();

    TOKEN getToken();

    void setToken(TOKEN token);

    String getUserName();

    Boolean isDirty();

}
