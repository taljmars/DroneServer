package com.auditdb.persistence.base_scheme;

import com.db.persistence.scheme.BaseObject;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.springframework.data.annotation.Transient;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;
import java.util.Objects;

public abstract class EventLogObject extends BaseObject {

    private String eventCode;
    private Date eventTime;

    public EventLogObject() {
        super();
    }

    public EventLogObject(EventLogObject auditLog) {
        super(auditLog);
        EventLogObject tmp = (EventLogObject) auditLog.copy();
        this.eventCode = tmp.eventCode;
        this.eventTime = tmp.eventTime;
    }

    @Override
    public abstract EventLogObject clone();

    @Override
    public BaseObject copy() {
        EventLogObject auditLog = (EventLogObject) super.copy();
        auditLog.eventCode = this.eventCode;
        auditLog.eventTime = this.eventTime;
        return auditLog;
    }

    @Override
    public void set(BaseObject baseObject) {
        EventLogObject auditLog = (EventLogObject) baseObject.copy();
        this.eventCode = auditLog.eventCode;
        this.eventTime = auditLog.eventTime;
    }

    @XmlTransient
    @Transient
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }


    @Getter
    public String getEventCode() {
        return eventCode;
    }

    @Setter
    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EventLogObject that = (EventLogObject) o;
        return Objects.equals(eventCode, that.eventCode) &&
                Objects.equals(eventTime, that.eventTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), eventCode, eventTime);
    }

    @Override
    public String toString() {
        return "EventLogObject{" +
                "eventCode='" + eventCode + '\'' +
                ", eventTime=" + eventTime +
                ", deleted=" + deleted +
                ", fromRevision=" + fromRevision +
                ", clz=" + clz +
                '}';
    }
}
