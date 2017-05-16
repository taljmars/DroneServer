package com.dronedb.persistence.scheme;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by taljmars on 3/19/17.
 */
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "GetAllPolygonPerimeters",
                query = "select * from polygonperimeter",
                resultClass = PolygonPerimeter.class
        )
})
@Table
@Entity
@Access(javax.persistence.AccessType.FIELD)
public class PolygonPerimeter extends Perimeter implements Serializable {

//    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
//    @JoinTable( name = "polygonperimeter_point",
//            joinColumns = @JoinColumn(name = "perimeter_id", referencedColumnName = "objid"),
//            inverseJoinColumns = @JoinColumn(name = "point_id", referencedColumnName="objid"))
    @ElementCollection
    protected List<UUID> points;

    public PolygonPerimeter() {
        super();
        this.points = new ArrayList<>();
    }

    public PolygonPerimeter(PolygonPerimeter polygonPerimeter) {
        super(polygonPerimeter);
        this.points = new ArrayList<>();
        for (UUID point : polygonPerimeter.getPoints())
            this.points.add(point);
    }

    @Override
    public PolygonPerimeter clone() {
        return new PolygonPerimeter(this);
    }

    @Override
    public BaseObject copy() {
        PolygonPerimeter polygonPerimeter = this.clone();
        polygonPerimeter.setKeyId(this.getKeyId());
        return polygonPerimeter;
    }

    @Override
    public void set(BaseObject baseObject) {
        super.set(baseObject);
        PolygonPerimeter polygonPerimeter = (PolygonPerimeter) baseObject;
        this.points = polygonPerimeter.getPoints();
    }

    @Getter
    public List<UUID> getPoints() {
        return points;
    }

    @Setter
    public void setPoints(List<UUID> points) {
        this.points = points;
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
