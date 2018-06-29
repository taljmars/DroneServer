/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.gui.persistence.scheme;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.Sessionable;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Created by taljmars on 3/20/17.
 */
@Table
@Entity
@Sessionable
public class Shape extends BaseObject
{
    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lon;

    public Shape() { super(); }

    public Shape(double lat, double lon) {
        super();
        this.lat = lat;
        this.lon = lon;
    }

    public Shape(Shape point) {
        super(point);
        this.lat = point.getLat();
        this.lon = point.getLon();
    }

    @Override
    public Shape clone() {
        return new Shape(this);
    }

    @Override
    public BaseObject copy() {
//        Point point = this.clone();
//        point.getKeyId().setObjId(this.getKeyId().getObjId());
        Shape point = (Shape) super.copy();
        return point;
    }

    @Override
    public void set(BaseObject baseObject) {
        Shape point = (Shape) baseObject;
        this.lon = point.getLon();
        this.lat = point.getLat();
    }

    @Getter
    public double getLat() {
        return lat;
    }

    @Setter
    public void setLat(double lat) {
        this.lat = lat;
    }

    @Getter
    public double getLon() {
        return lon;
    }

    @Setter
    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Shape shape = (Shape) o;
        return Double.compare(shape.lat, lat) == 0 &&
                Double.compare(shape.lon, lon) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), lat, lon);
    }

    @Override
    public String toString() {
        return "Shape{" +
                super.toString() +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
