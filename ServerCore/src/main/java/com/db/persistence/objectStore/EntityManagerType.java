package com.db.persistence.objectStore;

public enum EntityManagerType {

    MAIN_ENTITY_MANAGER(0),
    VIRTUALIZED_ENTITY_MANAGER(Integer.MAX_VALUE),
    UNKNOWN_ENTITY_MANAGER(-1);

    public Integer id;
    EntityManagerType(Integer id) {
        this.id = id;
    }
}
