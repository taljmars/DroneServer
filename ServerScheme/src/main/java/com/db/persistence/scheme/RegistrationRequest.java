package com.db.persistence.scheme;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement
public class RegistrationRequest {

    private String userName;
    private String password;

    public RegistrationRequest() {
    }

    @Getter
    @XmlElement(required = true)
    public String getPassword() {
        return password;
    }

    @Setter
    public void setPassword(String password) {
        this.password = password;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationRequest that = (RegistrationRequest) o;
        return Objects.equals(userName, that.userName) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password);
    }

    @Override
    public String toString() {
        return "SigningRequest{" +
                "userName='" + userName + '\'' +
                "password='" + password + '\'' +
                '}';
    }
}
