package com.dronedb.persistence.scheme;

import com.db.persistence.scheme.BaseObject;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

/**
 * Created by taljmars on 3/20/17.
 */
public class Point extends BaseObject {

    private Double lat;

    private Double lon;

    public Point() {
        super();
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
