package com.db.persistence.scheme;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by taljmars on 5/9/17.
 */
//@XmlRootElement(namespace = "scheme.persistence.db.com")
@Table
@Access(AccessType.FIELD)
@Entity
//@XmlType
public class ObjectDeref extends BaseObject {

    public ObjectDeref() {
        super();
    }

    public ObjectDeref(ObjectDeref objectDeref) {
        super(objectDeref);
        this.clzType = objectDeref.clzType;
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

    @Override
    public ObjectDeref clone() {
        return new ObjectDeref(this);
    }

    @Override
    public ObjectDeref copy() {
//        ObjectDeref objectDeref = this.clone();
//        objectDeref.setKeyId(this.getKeyId().copy());
        ObjectDeref objectDeref = (ObjectDeref) super.copy();
        return objectDeref;
    }

    @Override
    public void set(BaseObject baseObject) {
        System.out.println("Setting deref");
        ObjectDeref objectDeref = (ObjectDeref) baseObject;
        this.clz = ((ObjectDeref)baseObject).getClz();
    }

    @Override
    public String toString() {
        return "ObjectDeref{" +
                super.toString() +
                ", clzType=" + clzType +
                '}';
    }
}
