package com.dronedb.persistence.scheme;

import com.db.persistence.scheme.BaseObject;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;

/**
 * Created by taljmars on 3/19/17.
 */
public abstract class Perimeter extends BaseObject {

    protected String name;

    public Perimeter() {
        super();
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
