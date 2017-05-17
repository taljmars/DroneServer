package com.dronedb.persistence.scheme;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by oem on 5/9/17.
 */
@Table
@Access(AccessType.FIELD)
@Entity
public class ObjectDeref extends BaseObject {

    public ObjectDeref() {
        super();
    }

    public ObjectDeref(ObjectDeref objectDeref) {
        super(objectDeref);
        this.clz = objectDeref.clz;
    }

    @Column(nullable = true)
    private Class<? extends BaseObject> clz;

    @Getter
    public Class<? extends BaseObject> getClz() {
        return clz;
    }

    @Setter
    public void setClz(Class<? extends BaseObject> clz) {
        this.clz = clz;
    }

    @Override
    public ObjectDeref clone() {
        return new ObjectDeref(this);
    }

    @Override
    public ObjectDeref copy() {
        ObjectDeref objectDeref = this.clone();
        objectDeref.setKeyId(this.getKeyId().copy());
        return objectDeref;
    }

    @Override
    public void set(BaseObject baseObject) {
        System.out.println("Setting mission");
        ObjectDeref objectDeref = (ObjectDeref) baseObject;
        this.clz = ((ObjectDeref)baseObject).getClz();
    }
}
