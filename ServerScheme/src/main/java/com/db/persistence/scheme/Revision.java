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
public class Revision extends BaseObject implements Serializable {

    public Revision() {
        super();
    }

    public Revision(Revision revision) {
        super(revision);
        this.currentRevision = revision.currentRevision;
    }

    @Override
    public Revision clone() {
        return new Revision(this);
    }

    @Override
    public void set(BaseObject baseObject) {
        Revision revision = (Revision) baseObject;
        this.setCurrentRevision(revision.getCurrentRevision());
    }

    @Override
    public Revision copy() {
        Revision revision = (Revision) super.copy();
        return revision;
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
        if (!super.equals(o)) return false;

        Revision revision = (Revision) o;

        return currentRevision == revision.currentRevision;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + currentRevision;
        return result;
    }

    @Override
    public String toString() {
        return "Revision{" +
                "currentRevision=" + currentRevision +
                ", deleted=" + deleted +
                ", fromRevision=" + fromRevision +
                ", clz=" + clz +
                '}';
    }
}
