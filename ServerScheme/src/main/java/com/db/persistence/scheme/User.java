package com.db.persistence.scheme;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import java.util.Objects;

import static com.db.persistence.scheme.Constants.GEN_CTX;

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "GetUserByName",
                query = "SELECT * FROM User WHERE " + GEN_CTX + " AND (userName ilike :USERNAME)",
                resultClass = User.class
        )
})
@Table
@XmlAccessorType(XmlAccessType.PROPERTY)
@Entity
@Sessionable
public class User extends BaseObject {

    public User() {
        super();
    }

    public User(User objectDeref) {
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
    public User clone() {
        return new User(this);
    }

    @Override
    public void set(BaseObject baseObject) {
        User dummyBaseObject = (User) baseObject;
        this.setUserName(dummyBaseObject.getUserName());
    }


    @Override
    public User copy() {
        User objectDeref = (User) super.copy();
        return objectDeref;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
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
