package com.dronedb.persistence.scheme;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by oem on 4/28/17.
 */
@Embeddable
public class KeyId implements Serializable{

    public KeyId() {
        this.objId = UUID.randomUUID();
        this.privatelyModified = true;
        this.toRevision = Integer.MAX_VALUE;
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

    protected boolean privatelyModified;

    @Getter
    @Basic(optional = false)
    @XmlTransient
    @Column(nullable = false)
    public boolean getPrivatelyModified() {
        return privatelyModified;
    }

    @Setter
    public void setPrivatelyModified(boolean isModified) {
        this.privatelyModified = isModified;
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
        keyId.setToRevision(this.getToRevision());
        return keyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyId keyId = (KeyId) o;

        if (privatelyModified != keyId.privatelyModified) return false;
        if (toRevision != keyId.toRevision) return false;
        return objId != null ? objId.equals(keyId.objId) : keyId.objId == null;
    }

    @Override
    public int hashCode() {
        int result = objId != null ? objId.hashCode() : 0;
        result = 31 * result + (privatelyModified ? 1 : 0);
        result = 31 * result + toRevision;
        return result;
    }

    @Override
    public String toString() {
        return "KeyId{" +
                "objId=" + objId +
                ", privatelyModified=" + privatelyModified +
                ", toRevision=" + toRevision +
                '}';
    }
}
