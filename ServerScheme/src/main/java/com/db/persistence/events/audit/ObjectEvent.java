package com.db.persistence.events.audit;

import com.db.persistence.events.ServerEvent;
import com.db.persistence.scheme.BaseObject;

import java.util.UUID;

@ServerEvent(id = 8)
public class ObjectEvent implements AuditEvent {

    public enum ObjectEventType {
        CREATE,
        UPDATE,
        DELETE
    }

    private final ObjectEventType objectEventType;
    private final BaseObject tip;
    private final BaseObject item;
    private final int nextRevision;
    private final String userName;

    public <T extends BaseObject> ObjectEvent(ObjectEventType objectEventType, T tip, T item, int nextRevision, String userName) {
        this.objectEventType = objectEventType;
        this.tip = tip;
        this.item = item;
        this.nextRevision = nextRevision;
        this.userName = userName;
    }

    public BaseObject getTip() {
        return tip;
    }

    public BaseObject getItem() {
        return item;
    }

    public int getNextRevision() {
        return nextRevision;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    public ObjectEventType getObjectEventType() {
        return objectEventType;
    }

    @Override
    public String toString() {
        return "ObjectEvent{" +
                "objectEventType='" + objectEventType + '\'' +
                ", tip=" + tip +
                ", item=" + item +
                ", nextRevision=" + nextRevision +
                ", userName=" + userName +
                '}';
    }
}
