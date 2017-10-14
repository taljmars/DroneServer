package com.dronedb.persistence.scheme;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import java.util.UUID;

/**
 * Created by taljmars on 3/19/17.
 */
public class CirclePerimeter extends Perimeter {

    protected UUID center;

    protected Double radius;

    public CirclePerimeter() {
        super();
    }

    @Getter
    public UUID getCenter() {
        return center;
    }

    @Setter
    public void setCenter(UUID center) {
        this.center = center;
    }

    @Getter
    public Double getRadius() {
        return radius;
    }

    @Setter
    public void setRadius(Double radius) {
        this.radius = radius;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CirclePerimeter that = (CirclePerimeter) o;

        if (center != null ? !center.equals(that.center) : that.center != null) return false;
        return radius != null ? radius.equals(that.radius) : that.radius == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (center != null ? center.hashCode() : 0);
        result = 31 * result + (radius != null ? radius.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CirclePerimeter{" +
                super.toString() +
                "center=" + center +
                ", radius=" + radius +
                '}';
    }
}
