package com.db.persistence.events.audit;

import com.db.persistence.events.ServerEvent;

import java.util.Date;
import java.util.UUID;

@ServerEvent(id = 5)
public class AccessEvent implements AuditEvent {

    public enum AccessEventType{
        LOGIN,
        LOGOUT,
        TIMEOUT
    }

    private final AccessEventType eventType;
    private final String userName;
    private final Date date;
    private final Integer res;
    private final String msg;

    public AccessEvent(AccessEventType eventType, String userName, Date date, Integer returnCode, String message) {
        this.eventType = eventType;
        this.userName = userName;
        this.date = date;
        this.res = returnCode;
        this.msg = message;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    public Date getDate() {
        return date;
    }

    public Integer getRes() {
        return res;
    }

    public String getMsg() {
        return msg;
    }

    public AccessEventType getEventType() {
        return eventType;
    }

    @Override
    public String toString() {
        return "AccessEvent{" +
                "eventType='" + eventType + '\'' +
                ", userName='" + userName + '\'' +
                ", date=" + date +
                ", res=" + res +
                ", msg='" + msg + '\'' +
                '}';
    }
}
