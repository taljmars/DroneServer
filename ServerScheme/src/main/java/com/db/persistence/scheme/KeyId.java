package com.db.persistence.scheme;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.springframework.data.annotation.Transient;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by taljmars on 4/28/17.
 */
@XmlRootElement(namespace = "scheme.persistence.db.com")
@XmlAccessorType(XmlAccessType.PROPERTY)
@Embeddable
public class KeyId implements Serializable{

    public KeyId() {
        this.objId = UUID.randomUUID();
        this.toRevision = Integer.MAX_VALUE;
        this.entityManagerCtx = Integer.MAX_VALUE;
    }

    protected UUID objId;

    @Getter
    @Basic(optional = false)
    @XmlElement(required = true)
    @Column(nullable = false)
    public UUID getObjId() {
        return objId;
    }

    @Setter
    public void setObjId(UUID objId) {
        this.objId = objId;
    }

    private Integer entityManagerCtx;

    public Integer getEntityManagerCtx() {
        return entityManagerCtx;
    }

    @XmlTransient
	@Transient
    @Setter
    public void setEntityManagerCtx(Integer entityManagerCtx) {
        this.entityManagerCtx = entityManagerCtx;
    }


    protected int toRevision;

    @Getter
    public int getToRevision() {
        return toRevision;
    }

    @XmlTransient
    @Setter
    public void setToRevision(int toRevision) {
        this.toRevision = toRevision;
    }


    @Transient
    public KeyId copy() {
        KeyId keyId = new KeyId();
        keyId.setObjId(this.getObjId());
        keyId.setEntityManagerCtx(this.getEntityManagerCtx());
        keyId.setToRevision(this.getToRevision());
        return keyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyId keyId = (KeyId) o;

        if (toRevision != keyId.toRevision) return false;
        if (!objId.equals(keyId.objId)) return false;
        return entityManagerCtx.equals(keyId.entityManagerCtx);
    }

    @Override
    public int hashCode() {
        int result = objId.hashCode();
        result = 31 * result + entityManagerCtx.hashCode();
        result = 31 * result + toRevision;
        return result;
    }

    @Override
    public String toString() {
        return "KeyId{" +
                "objId=" + objId +
                ", entityManagerCtx=" + entityManagerCtx +
                ", toRevision=" + toRevision +
                '}';
    }
}
