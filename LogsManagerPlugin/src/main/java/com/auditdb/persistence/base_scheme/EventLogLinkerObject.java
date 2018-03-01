package com.auditdb.persistence.base_scheme;

import com.db.persistence.scheme.BaseObject;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.*;
import java.util.Objects;

@Access(AccessType.FIELD)
@Entity
@Table
public class EventLogLinkerObject extends BaseObject {

    private String eventCode;
    private String referedObj;
    private String type; //usualy table name

    public EventLogLinkerObject() {
        super();
    }

    public EventLogLinkerObject(EventLogLinkerObject auditLog) {
        super(auditLog);
        EventLogLinkerObject tmp = (EventLogLinkerObject) auditLog.copy();
        this.eventCode = tmp.eventCode;
        this.referedObj = tmp.referedObj;
        this.type = tmp.type;
    }

    @Override
    public EventLogLinkerObject clone() {
        return new EventLogLinkerObject(this);
    }

    @Override
    public BaseObject copy() {
        EventLogLinkerObject auditLog = (EventLogLinkerObject) super.copy();
        auditLog.eventCode = this.eventCode;
        auditLog.referedObj = this.referedObj;
        auditLog.type = this.type;
        return auditLog;
    }

    @Override
    public void set(BaseObject baseObject) {
        EventLogLinkerObject auditLog = (EventLogLinkerObject) baseObject.copy();
        this.eventCode = auditLog.eventCode;
        this.referedObj = auditLog.referedObj;
        this.type = auditLog.type;
    }

    @Getter
    public String getEventCode() {
        return eventCode;
    }

    @Setter
    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    @Getter
    public String getReferedObj() {
        return referedObj;
    }

    @Setter
    public void setReferedObj(String referedObj) {
        this.referedObj = referedObj;
    }

    @Getter
    public String getType() {
        return type;
    }

    @Setter
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EventLogLinkerObject auditLog = (EventLogLinkerObject) o;
        return Objects.equals(eventCode, auditLog.eventCode) &&
                Objects.equals(referedObj, auditLog.referedObj) &&
                Objects.equals(type, auditLog.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), eventCode, referedObj, type);
    }

    @Override
    public String toString() {
        return "AuditLog{" +
                "eventCode='" + eventCode + '\'' +
                ", referedObj='" + referedObj + '\'' +
                ", type=" + type +
                ", deleted=" + deleted +
                ", fromRevision=" + fromRevision +
                ", clz=" + clz +
                '}';
    }
}
