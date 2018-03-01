package com.db.persistence.objectStore;

import com.db.persistence.scheme.BaseObject;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

public interface EntityManagerBase {
    <T extends BaseObject> T find(Class<T> clz, String uuid);

    <T extends BaseObject> T delete(T object);

    <T extends BaseObject> T update(T object);

    <T extends BaseObject> TypedQuery<T> createNamedQuery(String queryString, Class<T> clz);

    Query createQuery(String queryString);

    <T extends BaseObject> Query createNativeQuery(String queryString, Class<T> clz);

    <T extends BaseObject> T pull(T obj);
}
