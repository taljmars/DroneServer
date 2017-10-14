package com.dronedb.persistence.scheme;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by taljmars on 3/19/17.
 */
public class PolygonPerimeter extends Perimeter {

    public PolygonPerimeter() {
        super();
        this.points = new ArrayList<>();
    }

    protected List<UUID> points;


    @Getter
    public List<UUID> getPoints() {
        return points;
    }

    @Setter
    public void setPoints(List<UUID> points) {
        this.points.clear();
        if (points != null)
            this.points.addAll(points);
    }

    public void addPoint(UUID point) {
        points.add(point);
    }

    public void removePoint(UUID point) {
        if (points != null)
            points.remove(point);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PolygonPerimeter that = (PolygonPerimeter) o;

        return points != null ? points.equals(that.points) : that.points == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (points != null ? points.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PolygonPerimeter{" +
                super.toString() +
                "points=" + points +
                '}';
    }
}
