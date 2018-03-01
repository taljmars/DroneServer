package com.db.persistence.events.audit;

import com.db.persistence.events.ServerEvent;
import com.db.persistence.scheme.BaseObject;

import java.util.UUID;

@ServerEvent(id = 8)
public class ObjectModificationEvent implements AuditEvent {

    private final BaseObject tip;
    private final BaseObject item;
    private final int nextRevision;

    public <T extends BaseObject> ObjectModificationEvent(T tip, T item, int nextRevision) {
        this.tip = tip;
        this.item = item;
        this.nextRevision = nextRevision;
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
    public String toString() {
        return "ObjectModificationEvent{" +
                "tip=" + tip +
                ", item=" + item +
                ", nextRevision=" + nextRevision +
                '}';
    }
}
