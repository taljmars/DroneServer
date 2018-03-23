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

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "GetAllObjectCreationLog",
                query = "SELECT * FROM ObjectCreationLog",
                resultClass = ObjectCreationLog.class
        ),
        @NamedNativeQuery(
                name = "GetAllObjectCreationLog_BetweenDates",
                query = "SELECT * FROM ObjectCreationLog WHERE eventTime BETWEEN :START_DATE AND :END_DATE",
                resultClass = ObjectCreationLog.class
        )
})
@Access(AccessType.FIELD)
@Entity
@Table
public class ObjectCreationLog extends EventLogObject {

    private String referredObjId;
    private Class referredObjType;

    public ObjectCreationLog() {
        super();
    }

    public ObjectCreationLog(ObjectCreationLog auditLog) {
        super(auditLog);
        this.referredObjId = auditLog.referredObjId;
        this.referredObjType = auditLog.referredObjType;
    }

    @Override
    public ObjectCreationLog clone() {
        return new ObjectCreationLog(this);
    }

    @Override
    public BaseObject copy() {
        ObjectCreationLog auditLog = (ObjectCreationLog) super.copy();
        auditLog.referredObjId = this.referredObjId;
        auditLog.referredObjType = this.referredObjType;
        return auditLog;
    }

    @Override
    public void set(BaseObject baseObject) {
        super.set(baseObject);
        ObjectCreationLog auditLog = (ObjectCreationLog) baseObject;
        this.referredObjId = auditLog.referredObjId;
        this.referredObjType = auditLog.referredObjType;
    }

    @Getter
    public String getReferredObjId() {
        return referredObjId;
    }

    @Setter
    public void setReferredObjId(String referredObjId) {
        this.referredObjId = referredObjId;
    }

    public Class getReferredObjType() {
        return referredObjType;
    }

    public void setReferredObjType(Class referredObjType) {
        this.referredObjType = referredObjType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ObjectCreationLog auditLog = (ObjectCreationLog) o;
        return Objects.equals(referredObjId, auditLog.referredObjId) &&
                Objects.equals(referredObjType, auditLog.referredObjType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), referredObjId, referredObjType);
    }

    @Override
    public String toString() {
        return "ObjectCreationLog{" +
                super.toString() +
                ", referredObjId='" + referredObjId + '\'' +
                ", referredObjType='" + referredObjType.getCanonicalName() + '\'' +
                ", deleted=" + deleted +
                ", fromRevision=" + fromRevision +
                ", clz=" + clz +
                '}';
    }
}
