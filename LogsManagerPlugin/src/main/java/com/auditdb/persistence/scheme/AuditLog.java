package com.auditdb.persistence.scheme;

import com.auditdb.persistence.base_scheme.EventLogObject;
import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.TargetType;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.*;
import java.util.*;

@Access(javax.persistence.AccessType.FIELD)
@Entity
@Table
public class AuditLog extends EventLogObject {

    private String referredObjId;

    private String userName;

    @TargetType(clz = String.class)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> changedFields;

    @TargetType(clz = String.class)
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> changedValues;

    public AuditLog() {
        super();
        changedFields = new ArrayList<>();
        changedValues = new ArrayList<>();
    }

    public AuditLog(AuditLog auditLog) {
        super(auditLog);
        AuditLog tmp = (AuditLog) auditLog.copy();
        this.referredObjId = tmp.referredObjId;
        this.changedFields = tmp.changedFields;
        this.changedValues = tmp.changedValues;
        this.userName = tmp.userName;
    }

    @Override
    public AuditLog clone() {
        return new AuditLog(this);
    }

    @Override
    public BaseObject copy() {
        AuditLog auditLog = (AuditLog) super.copy();
        auditLog.referredObjId = this.referredObjId;
        auditLog.userName = this.userName;
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
        AuditLog auditLog = (AuditLog) baseObject.copy();
        this.referredObjId = auditLog.referredObjId;
        this.changedFields = auditLog.changedFields;
        this.changedValues = auditLog.changedValues;
        this.userName = auditLog.userName;
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
    public String getUserName() {
        return userName;
    }

    @Setter
    public void setUserName(String userName) {
        this.userName = userName;
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
        AuditLog auditLog = (AuditLog) o;
        return Objects.equals(referredObjId, auditLog.referredObjId) &&
                Objects.equals(userName, auditLog.userName) &&
                Objects.equals(changedFields, auditLog.changedFields) &&
                Objects.equals(changedValues, auditLog.changedValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), referredObjId, userName, changedFields, changedValues);
    }

    @Override
    public String toString() {
        return "AuditLog{" +
                "referredObjId='" + referredObjId + '\'' +
                ", userName='" + userName + '\'' +
                ", changedFields=" + changedFields +
                ", changedValues=" + changedValues +
                ", deleted=" + deleted +
                ", fromRevision=" + fromRevision +
                ", clz=" + clz +
                '}';
    }
}
