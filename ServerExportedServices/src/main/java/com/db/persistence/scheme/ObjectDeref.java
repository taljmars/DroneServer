package com.db.persistence.scheme;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

/**
 * Created by taljmars on 5/9/17.
 */
public class ObjectDeref extends BaseObject {

    public ObjectDeref() {
        super();
    }

//    @Column(nullable = true)
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
    public String toString() {
        return "ObjectDeref{" +
                "clzType=" + clzType +
                '}';
    }
}
