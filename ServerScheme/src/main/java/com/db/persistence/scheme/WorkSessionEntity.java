package com.db.persistence.scheme;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Table
@XmlAccessorType(XmlAccessType.PROPERTY)
@Entity
@Sessionable
public class WorkSessionEntity extends BaseObject {

    private String userName;
    private Integer referredEntityManagerCtx;
    private Boolean dirty;

    public WorkSessionEntity() {super();}

    public WorkSessionEntity(WorkSessionEntity workSessionEntity) {
        super(workSessionEntity);
        this.userName = workSessionEntity.userName;
        this.referredEntityManagerCtx = workSessionEntity.referredEntityManagerCtx;
        this.dirty = workSessionEntity.dirty;
    }

    @Override
    public WorkSessionEntity clone() {
        return new WorkSessionEntity(this);
    }

    @Override
    public WorkSessionEntity copy() {
        WorkSessionEntity objectDeref = (WorkSessionEntity) super.copy();
        return objectDeref;
    }

    @Override
    public void set(BaseObject arg) {
        WorkSessionEntity workSessionEntity = (WorkSessionEntity) arg;
        this.setUserName(workSessionEntity.userName);
        this.setReferredEntityManagerCtx(workSessionEntity.referredEntityManagerCtx);
        this.setDirty(workSessionEntity.dirty);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getReferredEntityManagerCtx() {
        return referredEntityManagerCtx;
    }

    public void setReferredEntityManagerCtx(Integer referredEntityManagerCtx) {
        this.referredEntityManagerCtx = referredEntityManagerCtx;
    }

    public void setDirty(Boolean dirty) {
        this.dirty = dirty;
    }

    public Boolean getDirty() {
        return dirty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        WorkSessionEntity that = (WorkSessionEntity) o;

        if (referredEntityManagerCtx != that.referredEntityManagerCtx) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        return dirty != null ? dirty.equals(that.dirty) : that.dirty == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + referredEntityManagerCtx;
        result = 31 * result + (dirty != null ? dirty.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "WorkSessionEntity{" +
                "userName='" + userName + '\'' +
                ", referredEntityManagerCtx=" + referredEntityManagerCtx +
                ", dirty=" + dirty +
                ", deleted=" + deleted +
                ", fromRevision=" + fromRevision +
                ", clz=" + clz +
                '}';
    }
}
