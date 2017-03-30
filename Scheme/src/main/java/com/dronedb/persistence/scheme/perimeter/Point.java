package com.dronedb.persistence.scheme.perimeter;

import com.dronedb.persistence.scheme.BaseObject;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by taljmars on 3/20/17.
 */
@Entity
public class Point extends BaseObject {

    @Column(nullable = true)
    private Double lat;

    @Column(nullable = true)
    private Double lon;

    public Point() {}

    public Point(Point point) {
        this.lat = point.getLat();
        this.lon = point.getLon();
    }

    @Override
    public Point clone() {
        return new Point(this);
    }

    @Override
    public BaseObject copy() {
        Point point = this.clone();
        point.objId = this.objId;
        return point;
    }

    @Override
    public void set(BaseObject baseObject) {
        Point point = (Point) baseObject;
        this.lon = point.getLon();
        this.lat = point.getLat();
    }

    public Point(Double lat, Double lon) {
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

        Point point = (Point) o;

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
        return "Point{" +
                super.toString() +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
