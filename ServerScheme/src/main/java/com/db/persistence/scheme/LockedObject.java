package com.db.persistence.scheme;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.UUID;

/**
 * Created by taljmars on 5/9/17.
 */
//@XmlRootElement(namespace = "scheme.persistence.db.com")
@Table
@Access(AccessType.FIELD)
@Entity
//@XmlType
public class LockedObject extends BaseObject {

    public LockedObject() {
        super();
    }

    public LockedObject(LockedObject lockedObject) {
        super(lockedObject);
        this.clzType = lockedObject.clzType;
        this.referredObjId = lockedObject.referredObjId;
        this.referredCtx = lockedObject.referredCtx;
    }

    @Column(nullable = true)
    private Class<? extends BaseObject> clzType;

    @Getter
    public Class<? extends BaseObject> getClzType() {
        return clzType;
    }

    @Setter
    public void setClzType(Class<? extends BaseObject> clzType) {
        this.clzType = clzType;
    }

    private Integer referredCtx;

    @Getter
    @Column(nullable = false)
    public Integer getReferredCtx() {
        return referredCtx;
    }

    @XmlTransient
    @org.springframework.data.annotation.Transient
    @Setter
    public void setReferredCtx(Integer referredCtx) {
        this.referredCtx = referredCtx;
    }

    private String referredObjId;

    @Getter
    @Column(name = "referredObjId", nullable = false)
    public String getReferredObjId() {
        return referredObjId;
    }

    @XmlTransient
    @org.springframework.data.annotation.Transient
    @Setter
    public void setReferredObjId(String referredObjId) {
        this.referredObjId = referredObjId;
    }

    @Override
    public LockedObject clone() {
        return new LockedObject(this);
    }

    @Override
    public LockedObject copy() {
//        ObjectDeref objectDeref = this.clone();
//        objectDeref.setKeyId(this.getKeyId().copy());
        LockedObject lockedObject = (LockedObject) super.copy();

        return lockedObject;
    }

    @Override
    public void set(BaseObject baseObject) {
        LockedObject lockedObject = (LockedObject) baseObject;
        this.clz = lockedObject.getClz();
        this.referredCtx = lockedObject.referredCtx;
        this.referredObjId = lockedObject.referredObjId;
    }

    @Override
    public String toString() {
        return "LockedObject{" +
                "clzType=" + clzType +
                ", referredCtx=" + referredCtx +
                ", referredObjId=" + referredObjId +
                ", deleted=" + deleted +
                ", fromRevision=" + fromRevision +
                ", clz=" + clz +
                '}';
    }
}

