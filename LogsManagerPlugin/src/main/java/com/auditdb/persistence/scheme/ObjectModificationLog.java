package com.auditdb.persistence.scheme;

import com.auditdb.persistence.base_scheme.EventLogObject;
import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.TargetType;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.*;
import java.util.*;

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "GetAllObjectModificationLog",
                query = "SELECT * FROM ObjectModificationLog",
                resultClass = ObjectModificationLog.class
        ),
        @NamedNativeQuery(
                name = "GetAllObjectModificationLog_BetweenDates",
                query = "SELECT * FROM ObjectModificationLog WHERE eventTime BETWEEN :START_DATE AND :END_DATE",
                resultClass = ObjectModificationLog.class
        )
})
@Access(javax.persistence.AccessType.FIELD)
@Entity
@Table
public class ObjectModificationLog extends EventLogObject {

    private String referredObjId;

    @TargetType(clz = String.class)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> changedFields;

    @TargetType(clz = String.class)
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> changedValues;

    public ObjectModificationLog() {
        super();
        changedFields = new ArrayList<>();
        changedValues = new ArrayList<>();
    }

    public ObjectModificationLog(ObjectModificationLog auditLog) {
        super(auditLog);
        this.referredObjId = auditLog.referredObjId;
        this.changedFields = auditLog.changedFields;
        this.changedValues = auditLog.changedValues;
    }

    @Override
    public ObjectModificationLog clone() {
        return new ObjectModificationLog(this);
    }

    @Override
    public BaseObject copy() {
        ObjectModificationLog auditLog = (ObjectModificationLog) super.copy();
        auditLog.referredObjId = this.referredObjId;
        auditLog.changedFields = new ArrayList<>();
        for (String entry : this.changedFields) {
            auditLog.changedFields.add(entry);
        }
        auditLog.changedValues = new ArrayList<>();
        for (String object : this.changedValues) {
            auditLog.changedValues.add(object);
        }
        return auditLog;
    }

    @Override
    public void set(BaseObject baseObject) {
        super.set(baseObject);
        ObjectModificationLog auditLog = (ObjectModificationLog) baseObject;
        this.referredObjId = auditLog.referredObjId;
        this.changedFields = auditLog.changedFields;
        this.changedValues = auditLog.changedValues;
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
    public List<String> getChangedFields() {
        return changedFields;
    }

    @Setter
    public void setChangedFields(List<String> changedFields) {
        this.changedFields = changedFields;
    }

    @Getter
    public List<String> getChangedValues() {
        return changedValues;
    }

    @Setter
    public void setChangedValues(List<String> changedValues) {
        this.changedValues = changedValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ObjectModificationLog auditLog = (ObjectModificationLog) o;
        return Objects.equals(referredObjId, auditLog.referredObjId) &&
                Objects.equals(changedFields, auditLog.changedFields) &&
                Objects.equals(changedValues, auditLog.changedValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), referredObjId, changedFields, changedValues);
    }

    @Override
    public String toString() {
        return "AuditLog{" +
                super.toString() +
                ", referredObjId='" + referredObjId + '\'' +
                ", changedFields=" + changedFields +
                ", changedValues=" + changedValues +
                ", deleted=" + deleted +
                ", fromRevision=" + fromRevision +
                ", clz=" + clz +
                '}';
    }
}
