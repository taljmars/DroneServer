package com.db.server.security;

import org.springframework.security.core.session.SessionInformation;

import java.util.Date;

public class MySessionInformation {//extends SessionInformation {

    public static final Integer DEFAULT_TIMEOUT = 30;

    private final SessionInformation sessionInformation;

    private Integer timeout;
    private String applicationName;
    private final String sessionId;
    private final String principal;

    public MySessionInformation(SessionInformation sessionInformation) {
        this.sessionInformation = sessionInformation;
        this.sessionId = sessionInformation.getSessionId();
        this.principal = ServerSessionRegistry.getPrincipalName(this.sessionInformation.getPrincipal());
        this.timeout = DEFAULT_TIMEOUT;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getPrincipal() {
        return principal;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void expireNow() {
        this.sessionInformation.expireNow();
    }

    public void refreshLastRequest() {
        this.sessionInformation.refreshLastRequest();
    }

    public Date getLastRequest() {
        return this.sessionInformation.getLastRequest();
    }

    public SessionInformation getCoreObject() {
        return sessionInformation;
    }
}
