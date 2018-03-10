package com.auditdb.persistence.scheme;

import com.auditdb.persistence.base_scheme.EventLogObject;
import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.TargetType;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.db.persistence.scheme.Constants.GEN_CTX;

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "GetAllAccessLog",
                query = "SELECT * FROM AccessLog",
                resultClass = AccessLog.class
        ),
        @NamedNativeQuery(
                name = "GetAllAccessLog_BetweenDates",
                query = "SELECT * FROM AccessLog WHERE eventTime BETWEEN :START_DATE AND :END_DATE",
                resultClass = AccessLog.class
        )
})
@Access(AccessType.FIELD)
@Entity
@Table
public class AccessLog extends EventLogObject {

    private Boolean isLogin;

    public AccessLog() {
        super();
        isLogin = true;
    }

    public AccessLog(AccessLog auditLog) {
        super(auditLog);
//        AccessLog tmp = (AccessLog) auditLog.copy();
        this.isLogin = auditLog.isLogin;
    }

    @Override
    public AccessLog clone() {
        return new AccessLog(this);
    }

    @Override
    public BaseObject copy() {
        AccessLog auditLog = (AccessLog) super.copy();
        auditLog.isLogin = this.isLogin;
        return auditLog;
    }

    @Override
    public void set(BaseObject baseObject) {
        super.set(baseObject);
        AccessLog auditLog = (AccessLog) baseObject;
        this.isLogin = auditLog.isLogin;
    }

    @Getter
    public Boolean isLogin() {
        return isLogin;
    }

    @Setter
    public void setLogin(Boolean isLogin) {
        this.isLogin = isLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AccessLog auditLog = (AccessLog) o;
        return  super.equals(o) && Objects.equals(isLogin, auditLog.isLogin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isLogin);
    }

    @Override
    public String toString() {
        return "AccessLog{" +
                super.toString() +
                ", isLogin=" + isLogin +
                ", deleted=" + deleted +
                ", fromRevision=" + fromRevision +
                ", clz=" + clz +
                '}';
    }
}
