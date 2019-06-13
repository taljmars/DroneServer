package com.auditdb.persistence.base_scheme;

import com.db.persistence.scheme.BaseObject;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.springframework.data.annotation.Transient;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;
import java.util.Objects;

@Entity
public abstract class EventLogObject extends BaseObject {

    private String userName;
    private String eventCode;
    private Date eventTime;

    public EventLogObject() {
        super();
    }

    public EventLogObject(EventLogObject auditLog) {
        super(auditLog);
        this.eventCode = auditLog.eventCode;
        this.eventTime = auditLog.eventTime;
        this.userName = auditLog.userName;
    }

    @Override
    public abstract EventLogObject clone();

    @Override
    public BaseObject copy() {
        EventLogObject auditLog = (EventLogObject) super.copy();
        auditLog.eventCode = this.eventCode;
        auditLog.eventTime = this.eventTime;
        auditLog.userName = this.userName;
        return auditLog;
    }

    @Override
    public void set(BaseObject baseObject) {
        EventLogObject auditLog = (EventLogObject) baseObject;
        this.eventCode = auditLog.eventCode;
        this.eventTime = auditLog.eventTime;
        this.userName = auditLog.userName;
    }

//    @XmlTransient
//    @Transient
    @Temporal(TemporalType.TIMESTAMP)
    @Getter
    public Date getEventTime() {
        return eventTime;
    }

    @XmlTransient
    @Transient
    @Setter
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

    @Getter
    public String getUserName() {
        return userName;
    }

    @Setter
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EventLogObject that = (EventLogObject) o;
        return  Objects.equals(userName, that.userName) &&
                Objects.equals(eventCode, that.eventCode) &&
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
                ", userName=" + userName +
                ", deleted=" + deleted +
                ", fromRevision=" + fromRevision +
                ", clz=" + clz +
                '}';
    }
}
