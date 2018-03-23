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
                name = "GetAllObjectUpdateLog",
                query = "SELECT * FROM ObjectUpdateLog",
                resultClass = ObjectUpdateLog.class
        ),
        @NamedNativeQuery(
                name = "GetAllObjectUpdateLog_BetweenDates",
                query = "SELECT * FROM ObjectUpdateLog WHERE eventTime BETWEEN :START_DATE AND :END_DATE",
                resultClass = ObjectUpdateLog.class
        )
})
@Access(javax.persistence.AccessType.FIELD)
@Entity
@Table
public class ObjectUpdateLog extends EventLogObject {

    private String referredObjId;
    private Class referredObjType;

    @TargetType(clz = String.class)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> changedFields;

    @TargetType(clz = String.class)
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> changedFromValues;

    @TargetType(clz = String.class)
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> changedToValues;

    public ObjectUpdateLog() {
        super();
        changedFields = new ArrayList<>();
        changedFromValues = new ArrayList<>();
        changedToValues = new ArrayList<>();
    }

    public ObjectUpdateLog(ObjectUpdateLog auditLog) {
        super(auditLog);
        this.referredObjId = auditLog.referredObjId;
        this.changedFields = auditLog.changedFields;
        this.changedFromValues = auditLog.changedFromValues;
        this.changedToValues = auditLog.changedToValues;
        this.referredObjType = auditLog.referredObjType;
    }

    @Override
    public ObjectUpdateLog clone() {
        return new ObjectUpdateLog(this);
    }

    @Override
    public BaseObject copy() {
        ObjectUpdateLog auditLog = (ObjectUpdateLog) super.copy();
        auditLog.referredObjId = this.referredObjId;
        auditLog.referredObjType = this.referredObjType;
        auditLog.changedFields = new ArrayList<>();
        for (String entry : this.changedFields) {
            auditLog.changedFields.add(entry);
        }
        auditLog.changedFromValues = new ArrayList<>();
        for (String object : this.changedFromValues) {
            auditLog.changedFromValues.add(object);
        }
        auditLog.changedToValues = new ArrayList<>();
        for (String object : this.changedToValues) {
            auditLog.changedToValues.add(object);
        }
        return auditLog;
    }

    @Override
    public void set(BaseObject baseObject) {
        super.set(baseObject);
        ObjectUpdateLog auditLog = (ObjectUpdateLog) baseObject;
        this.referredObjId = auditLog.referredObjId;
        this.referredObjType = auditLog.referredObjType;
        this.changedFields = auditLog.changedFields;
        this.changedFromValues = auditLog.changedFromValues;
        this.changedToValues = auditLog.changedToValues;
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
    public List<String> getChangedFromValues() {
        return changedFromValues;
    }

    @Setter
    public void setChangedFromValues(List<String> changedFromValues) {
        this.changedFromValues = changedFromValues;
    }

    @Getter
    public List<String> getChangedToValues() {
        return changedToValues;
    }

    @Setter
    public void setChangedToValues(List<String> changedToValues) {
        this.changedToValues = changedToValues;
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
        ObjectUpdateLog auditLog = (ObjectUpdateLog) o;
        return Objects.equals(referredObjId, auditLog.referredObjId) &&
                Objects.equals(referredObjType, auditLog.referredObjType) &&
                Objects.equals(changedFields, auditLog.changedFields) &&
                Objects.equals(changedFromValues, auditLog.changedFromValues) &&
                Objects.equals(changedToValues, auditLog.changedToValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), referredObjId, changedFields, changedFromValues, changedToValues, referredObjType);
    }

    @Override
    public String toString() {
        return "ObjectUpdateLog{" +
                super.toString() +
                ", referredObjId='" + referredObjId + '\'' +
                ", referredObjType='" + referredObjType.getCanonicalName() + '\'' +
                ", changedFields=" + changedFields +
                ", changedFromValues=" + changedFromValues +
                ", changedToValues=" + changedToValues +
                ", deleted=" + deleted +
                ", fromRevision=" + fromRevision +
                ", clz=" + clz +
                '}';
    }
}
