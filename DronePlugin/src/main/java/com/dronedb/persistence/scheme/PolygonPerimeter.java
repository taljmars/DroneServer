package com.dronedb.persistence.scheme;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.Sessionable;
import com.db.persistence.triggers.DeleteTrigger;
import com.db.persistence.triggers.DeleteTriggers;
import com.db.persistence.triggers.UpdateTrigger;
import com.db.persistence.triggers.UpdateTriggers;
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
@Entity
@Table
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "GetAllPolygonPerimeters",
                query = "SELECT * FROM polygonperimeter WHERE " + Constants.POLYGON_PERIMETER_QUERY_FROM_TIP_AND_PRIVATE,
                resultClass = PolygonPerimeter.class
        ),
        @NamedNativeQuery(
                name = "GetAllModifiedPolygonPerimeters",
                query = "SELECT * FROM polygonperimeter WHERE privatelyModified = true",
                resultClass = PolygonPerimeter.class
        )
})
@DeleteTriggers({
        @DeleteTrigger(trigger = "com.dronedb.persistence.triggers.HandlePerimeterDeletionTrigger")
})
@UpdateTriggers({
        @UpdateTrigger(trigger = "com.dronedb.persistence.triggers.HandleRedundantPointsTrigger", phase = UpdateTrigger.PHASE.UPDATE),
})
@Access(javax.persistence.AccessType.FIELD)
@Sessionable
public class PolygonPerimeter extends Perimeter implements Serializable {

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
//        PolygonPerimeter polygonPerimeter = this.clone();
//        polygonPerimeter.setKeyId(this.getKeyId().copy());
        PolygonPerimeter polygonPerimeter = (PolygonPerimeter) super.copy();
        return polygonPerimeter;
    }

    @Override
    public void set(BaseObject baseObject) {
        super.set(baseObject);
        PolygonPerimeter polygonPerimeter = (PolygonPerimeter) baseObject;
        this.points = new ArrayList<>();
        for (UUID missionItemUid : polygonPerimeter.getPoints()) {
            this.points.add(missionItemUid);
        }
    }

    @ElementCollection(fetch = FetchType.EAGER)
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
