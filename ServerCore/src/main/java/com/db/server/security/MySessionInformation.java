package com.db.server.security;

import java.util.Date;

public class MySessionInformation {

    public static final Integer DEFAULT_TIMEOUT = 30;

    private Integer timeout;
    private String applicationName;
    private String userName;
    private MyToken token;
    private Date refreshTime;
    private boolean expired;

    public MySessionInformation() {
        this.timeout = DEFAULT_TIMEOUT;
        this.refreshTime = new Date();
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
        long diff = ((new Date()).getTime() - this.getLastRequest().getTime()) / 1000;
        if (diff >= this.timeout) {
            token.revokeNow();
            expired = true;
        }

    }

    public void refreshLastRequest() {
        this.refreshTime = new Date();
    }

    public Date getLastRequest() {
        return refreshTime;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setToken(MyToken token) {
        this.token = token;
    }

    public MyToken getToken() {
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
