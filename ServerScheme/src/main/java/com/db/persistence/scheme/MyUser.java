package com.db.persistence.scheme;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import java.io.Serializable;
import java.util.Objects;

import static com.db.persistence.scheme.Constants.GEN_CTX;

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "GetUserByName",
                query = "SELECT * FROM MyUser WHERE " + GEN_CTX + " AND (userName ilike :USERNAME)",
                resultClass = MyUser.class
        )
})
@Table
@XmlAccessorType(XmlAccessType.PROPERTY)
@Entity
@Sessionable
public class MyUser extends BaseObject {

    public MyUser() {
        super();
    }

    public MyUser(MyUser objectDeref) {
        super(objectDeref);
        this.userName = objectDeref.getUserName();
    }

    private String userName;
    private String password;

    @Getter
    public String getUserName() {
        return userName;
    }

    @Setter
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Getter
    public String getPassword() {
        return password;
    }

    @Setter
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    @Transient
    public MyUser clone() {
        return new MyUser(this);
    }

    @Override
    public void set(BaseObject baseObject) {
        MyUser dummyBaseObject = (MyUser) baseObject;
        this.setUserName(dummyBaseObject.getUserName());
    }


    @Override
    public MyUser copy() {
        MyUser objectDeref = (MyUser) super.copy();
        return objectDeref;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MyUser user = (MyUser) o;
        return Objects.equals(userName, user.userName) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userName, password);
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", deleted=" + deleted +
                ", fromRevision=" + fromRevision +
                ", clz=" + clz +
                '}';
    }
}
