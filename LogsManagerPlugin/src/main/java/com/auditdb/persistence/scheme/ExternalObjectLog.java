/*
 * Tal Martsiano
 * Copyright (c) 2019.
 */

package com.auditdb.persistence.scheme;

import com.auditdb.persistence.base_scheme.EventLogObject;
import com.db.persistence.scheme.BaseObject;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "GetAllExternalObjectLog",
                query = "SELECT * FROM ExternalObjectLog",
                resultClass = ExternalObjectLog.class
        ),
        @NamedNativeQuery(
                name = "GetAllExternalObjectLog_BetweenDates",
                query = "SELECT * FROM ExternalObjectLog WHERE eventTime BETWEEN :START_DATE AND :END_DATE",
                resultClass = ExternalObjectLog.class
        )
})
@Access(AccessType.FIELD)
@Entity
@Table
public class ExternalObjectLog extends EventLogObject {

    private String eventSource;
    private Date date;
    private String type;
    private String externalUser;
    private String payload;
    private String topic;
    private String summary;

    public ExternalObjectLog() {
        super();
    }

    public ExternalObjectLog(ExternalObjectLog auditLog) {
        super(auditLog);
        this.eventSource = auditLog.eventSource;
        this.date = auditLog.date;
        this.type = auditLog.type;
        this.externalUser = auditLog.externalUser;
        this.payload = auditLog.payload;
        this.topic = auditLog.topic;
        this.summary = auditLog.summary;
    }

    @Override
    public ExternalObjectLog clone() {
        return new ExternalObjectLog(this);
    }

    @Override
    public BaseObject copy() {
        ExternalObjectLog auditLog = (ExternalObjectLog) super.copy();
        this.eventSource = this.eventSource;
        this.date = this.date;
        this.type = this.type;
        this.externalUser = this.externalUser;
        this.payload = this.payload;
        this.topic = this.topic;
        this.summary = this.summary;
        return auditLog;
    }

    @Override
    public void set(BaseObject baseObject) {
        super.set(baseObject);
        ExternalObjectLog auditLog = (ExternalObjectLog) baseObject;
        this.eventSource = auditLog.eventSource;
        this.date = auditLog.date;
        this.type = auditLog.type;
        this.externalUser = auditLog.externalUser;
        this.payload = auditLog.payload;
        this.topic = auditLog.topic;
        this.summary = auditLog.summary;
    }

    @Getter
    public String getEventSource() {
        return eventSource;
    }

    @Setter
    public void setEventSource(String eventSource) {
        this.eventSource = eventSource;
    }

    @Getter
    public Date getDate() {
        return date;
    }

    @Setter
    public void setDate(Date date) {
        this.date = date;
    }

    @Getter
    public String getType() {
        return type;
    }

    @Setter
    public void setType(String type) {
        this.type = type;
    }

    @Getter
    public String getExternalUser() {
        return externalUser;
    }

    @Setter
    public void setExternalUser(String externalUser) {
        this.externalUser = externalUser;
    }

    @Getter
    public String getPayload() {
        return payload;
    }

    @Setter
    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Getter
    public String getTopic() {
        return topic;
    }

    @Setter
    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Getter
    public String getSummary() {
        return summary;
    }

    @Setter
    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ExternalObjectLog that = (ExternalObjectLog) o;
        return Objects.equals(eventSource, that.eventSource) &&
                Objects.equals(date, that.date) &&
                Objects.equals(type, that.type) &&
                Objects.equals(externalUser, that.externalUser) &&
                Objects.equals(payload, that.payload) &&
                Objects.equals(topic, that.topic) &&
                Objects.equals(summary, that.summary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), eventSource, date, type, externalUser, payload, topic, summary);
    }

    @Override
    public String toString() {
        return "ExternalObjectLog{" +
                "eventSource='" + eventSource + '\'' +
                ", date=" + date +
                ", type='" + type + '\'' +
                ", externalUser='" + externalUser + '\'' +
                ", payload='" + payload + '\'' +
                ", topic='" + topic + '\'' +
                ", summary='" + summary + '\'' +
                '}';
    }
}
