package com.db.persistence.scheme;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement
public class LoginRequest {

    private String userName;
    private String applicationName;
    private Integer timeout;

    public LoginRequest() {
    }

    @Getter
    @XmlElement(required = true)
    public String getApplicationName() {
        return applicationName;
    }

    @Setter
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    @Getter
    @XmlElement(required = true)
    public String getUserName() {
        return userName;
    }

    @Setter
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Getter
    @XmlElement(required = true)
    public Integer getTimeout() {
        return timeout;
    }

    @Setter
    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginRequest that = (LoginRequest) o;
        return Objects.equals(timeout, that.timeout) &&
                Objects.equals(userName, that.userName) &&
                Objects.equals(applicationName, that.applicationName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userName, timeout, applicationName);
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "userName='" + userName + '\'' +
                "timeout='" + timeout + '\'' +
                ", applicationName='" + applicationName + '\'' +
                '}';
    }
}
