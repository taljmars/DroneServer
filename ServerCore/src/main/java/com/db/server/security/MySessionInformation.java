package com.db.server.security;

import java.time.Instant;
import java.util.Date;

public class MySessionInformation {

    public static final Integer DEFAULT_TIMEOUT = 30;

    private Integer timeout;
    private String applicationName;
    private String userName;
    private String token;
    private Date refreshTime;
    private boolean expired;

    public MySessionInformation() {
        this.timeout = DEFAULT_TIMEOUT;
        this.refreshTime = Date.from(Instant.now());
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

    public void expireNow() {
        if (Date.from(Instant.now()).getTime() - this.getLastRequest().getTime() >= this.timeout)
            expired = true;

    }

    public void refreshLastRequest() {
        this.refreshTime = Date.from(Instant.now());
    }

    public Date getLastRequest() {
        return Date.from(Instant.now());
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public boolean isExpired() {
        return expired;
    }

    @Override
    public String toString() {
        return "MySessionInformation{" +
                "timeout=" + timeout +
                ", applicationName='" + applicationName + '\'' +
                ", userName='" + userName + '\'' +
                ", token='" + token + '\'' +
                ", refreshTime=" + refreshTime +
                ", expired=" + expired +
                '}';
    }
}
