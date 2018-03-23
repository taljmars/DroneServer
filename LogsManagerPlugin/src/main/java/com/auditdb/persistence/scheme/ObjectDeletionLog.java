package com.auditdb.persistence.scheme;

import com.auditdb.persistence.base_scheme.EventLogObject;
import com.db.persistence.scheme.BaseObject;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.*;
import java.util.Objects;

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "GetAllObjectDeletionLog",
                query = "SELECT * FROM ObjectDeletionLog",
                resultClass = ObjectDeletionLog.class
        ),
        @NamedNativeQuery(
                name = "GetAllObjectDeletionLog_BetweenDates",
                query = "SELECT * FROM ObjectDeletionLog WHERE eventTime BETWEEN :START_DATE AND :END_DATE",
                resultClass = ObjectDeletionLog.class
        )
})
@Access(AccessType.FIELD)
@Entity
@Table
public class ObjectDeletionLog extends EventLogObject {

    private String referredObjId;
    private Class referredObjType;

    public ObjectDeletionLog() {
        super();
    }

    public ObjectDeletionLog(ObjectDeletionLog auditLog) {
        super(auditLog);
        this.referredObjId = auditLog.referredObjId;
        this.referredObjType = auditLog.referredObjType;
    }

    @Override
    public ObjectDeletionLog clone() {
        return new ObjectDeletionLog(this);
    }

    @Override
    public BaseObject copy() {
        ObjectDeletionLog auditLog = (ObjectDeletionLog) super.copy();
        auditLog.referredObjId = this.referredObjId;
        auditLog.referredObjType = this.referredObjType;
        return auditLog;
    }

    @Override
    public void set(BaseObject baseObject) {
        super.set(baseObject);
        ObjectDeletionLog auditLog = (ObjectDeletionLog) baseObject;
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

    @Getter
    public Class getReferredObjType() {
        return referredObjType;
    }

    @Setter
    public void setReferredObjType(Class referredObjType) {
        this.referredObjType = referredObjType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ObjectDeletionLog auditLog = (ObjectDeletionLog) o;
        return Objects.equals(referredObjId, auditLog.referredObjId) &&
                Objects.equals(referredObjType, auditLog.referredObjType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), referredObjId, referredObjType);
    }

    @Override
    public String toString() {
        return "ObjectDeletionLog{" +
                super.toString() +
                ", referredObjId='" + referredObjId + '\'' +
                ", referredObjType='" + referredObjType.getCanonicalName() + '\'' +
                ", deleted=" + deleted +
                ", fromRevision=" + fromRevision +
                ", clz=" + clz +
                '}';
    }
}
