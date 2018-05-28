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

/**
 * Created by taljmars on 3/20/17.
 */
@Table
@Entity
@Sessionable
public class Shape extends BaseObject
{

    @Column(nullable = true)
    private Double lat;

    @Column(nullable = true)
    private Double lon;

    public Shape() { super(); }

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

    public Shape(Double lat, Double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Getter
    public Double getLat() {
        return lat;
    }

    @Setter
    public void setLat(Double lat) {
        this.lat = lat;
    }

    @Getter
    public Double getLon() {
        return lon;
    }

    @Setter
    public void setLon(Double lon) {
        this.lon = lon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Shape point = (Shape) o;

        if (lat != null ? !lat.equals(point.lat) : point.lat != null) return false;
        return lon != null ? lon.equals(point.lon) : point.lon == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (lat != null ? lat.hashCode() : 0);
        result = 31 * result + (lon != null ? lon.hashCode() : 0);
        return result;
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
