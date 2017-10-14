package com.dronedb.persistence.scheme;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.triggers.DeleteTrigger;
import com.db.persistence.triggers.DeleteTriggers;
import com.db.persistence.triggers.UpdateTrigger;
import com.db.persistence.triggers.UpdateTriggers;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;

/**
 * Created by taljmars on 3/19/17.
 */
@XmlSeeAlso({PolygonPerimeter.class, CirclePerimeter.class})
@Entity
public abstract class Perimeter extends BaseObject implements Serializable {

    @Column(nullable = true)
    protected String name;

//    public Perimeter() {
//        super();
//    }

    public Perimeter() {}

    public Perimeter(Perimeter perimeter) {
        super(perimeter);
        this.name = perimeter.getName();
    }

    public abstract Perimeter clone();

    @Override
    public void set(BaseObject baseObject) {
        Perimeter perimeter = (Perimeter) baseObject;
        this.name = perimeter.getName();
    }

    @Override
    public BaseObject copy() {
        Perimeter perimeter = (Perimeter) super.copy();
        return perimeter;
    }

    @Getter
    public String getName() {
        return name;
    }

    @Setter
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Perimeter perimeter = (Perimeter) o;

        return name != null ? name.equals(perimeter.name) : perimeter.name == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Perimeter{" +
                super.toString() +
                "name='" + name + '\'' +
                '}';
    }
}
