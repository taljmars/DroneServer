package com.db.persistence.scheme;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import static com.db.persistence.scheme.Constants.GEN_CTX;

@NamedNativeQueries({
        @NamedNativeQuery(
                name = "GetAllDummyBaseObject",
                query = "SELECT * FROM DummyBaseObject WHERE " + GEN_CTX,
//                query = "SELECT * FROM DummyBaseObject",
                resultClass = DummyBaseObject.class
        )
})
@Table
@XmlAccessorType(XmlAccessType.PROPERTY)
@Entity
@Sessionable
public class DummyBaseObject extends BaseObject {

    public DummyBaseObject() {
        super();
    }

    public DummyBaseObject(DummyBaseObject objectDeref) {
        super(objectDeref);
        this.name = objectDeref.getName();
    }

    private String name;

    @Getter
    public String getName() {
        return name;
    }

    @Setter
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public DummyBaseObject clone() {
        return new DummyBaseObject(this);
    }

    @Override
    public void set(BaseObject baseObject) {
        DummyBaseObject dummyBaseObject = (DummyBaseObject) baseObject;
        this.setName(dummyBaseObject.getName());
    }


    @Override
    public DummyBaseObject copy() {
//        DummyBaseObject objectDeref = this.clone();
//        objectDeref.setKeyId(this.getKeyId().copy());
        DummyBaseObject objectDeref = (DummyBaseObject) super.copy();
        return objectDeref;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DummyBaseObject that = (DummyBaseObject) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DummyBaseObject{" +
                super.toString() +
                " name='" + name + '\'' +
                '}';
    }
}
