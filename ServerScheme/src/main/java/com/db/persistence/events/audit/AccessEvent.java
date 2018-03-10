package com.db.persistence.events.audit;

import com.db.persistence.events.ServerEvent;

import java.util.Date;
import java.util.UUID;

@ServerEvent(id = 5)
public class AccessEvent implements AuditEvent {

    private final String userName;
    private final Date date;
    private final Integer res;
    private final String msg;

    public AccessEvent(String userName, Date date, Integer returnCode, String message) {
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

    @Override
    public String toString() {
        return "AccessEvent{" +
                "userName='" + userName + '\'' +
                ", date=" + date +
                ", res=" + res +
                ", msg='" + msg + '\'' +
                '}';
    }
}
