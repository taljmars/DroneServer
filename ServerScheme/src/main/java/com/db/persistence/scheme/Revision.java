package com.db.persistence.scheme;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * Created by taljmars on 4/29/17.
 */
@XmlRootElement(namespace = "scheme.persistence.db.com")
@XmlAccessorType(XmlAccessType.PROPERTY)
@Entity
@Table
public class Revision implements Serializable {

    public Revision() {
        keyId = new KeyId();
    }

    private KeyId keyId;

    @EmbeddedId
    @Basic(optional = false)
    @Column(nullable = false)
    @Getter
    public KeyId getKeyId() {
        return keyId;
    }

    @Setter
    public void setKeyId(KeyId keyId) {
        this.keyId = keyId;
    }

    private int currentRevision;

    @Getter
    public int getCurrentRevision() {
        return currentRevision;
    }

    @XmlTransient
    @Setter
    public void setCurrentRevision(int currentRevision) {
        this.currentRevision = currentRevision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Revision revision = (Revision) o;

        if (currentRevision != revision.currentRevision) return false;
        return keyId != null ? keyId.equals(revision.keyId) : revision.keyId == null;
    }

    @Override
    public int hashCode() {
        int result = keyId != null ? keyId.hashCode() : 0;
        result = 31 * result + currentRevision;
        return result;
    }

    @Override
    public String toString() {
        return "Revision{" +
                "keyId=" + keyId +
                ", currentRevision=" + currentRevision +
                '}';
    }
}
