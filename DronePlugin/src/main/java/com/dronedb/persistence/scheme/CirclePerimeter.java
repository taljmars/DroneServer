package com.dronedb.persistence.scheme;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.Sessionable;
import com.db.persistence.scheme.TargetType;
import com.db.persistence.triggers.DeleteTrigger;
import com.db.persistence.triggers.DeleteTriggers;
import com.db.persistence.triggers.UpdateTrigger;
import com.db.persistence.triggers.UpdateTriggers;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.*;
import java.io.Serializable;

import static com.db.persistence.scheme.Constants.GEN_CTX;


/**
 * Created by taljmars on 3/19/17.
 */
@Entity
@Table
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "GetAllCirclePerimeters",
//                query = "SELECT * FROM CirclePerimeter WHERE " + CIRCLE_PERIMETER_QUERY_FROM_TIP_AND_PRIVATE,
                query = "SELECT * FROM CirclePerimeter WHERE " + GEN_CTX,
                resultClass = CirclePerimeter.class
        ),
        @NamedNativeQuery(
                name = "GetAllModifiedCirclePerimeters",
                query = "SELECT * FROM CirclePerimeter WHERE " + GEN_CTX + " AND (entityManagerCtx != 0)",
                resultClass = CirclePerimeter.class
        )
})
@DeleteTriggers({
        @DeleteTrigger(trigger = "com.dronedb.persistence.triggers.HandlePerimeterDeletionTrigger")
})
@UpdateTriggers({
        @UpdateTrigger(trigger = "com.dronedb.persistence.triggers.HandleRedundantPointsTrigger", phase = UpdateTrigger.PHASE.UPDATE),
})
//@Access(javax.persistence.AccessType.FIELD)
@Sessionable
public class CirclePerimeter extends Perimeter implements Serializable {

    public CirclePerimeter() {super();}

    public CirclePerimeter(CirclePerimeter circlePerimeter) {
        super(circlePerimeter);
        this.center = circlePerimeter.getCenter();
        this.radius = circlePerimeter.getRadius();
    }

    @Override
    public CirclePerimeter clone() {
        return new CirclePerimeter(this);
    }

    @Column(nullable = true)
    @Basic(fetch=FetchType.EAGER)
    @TargetType(clz = Point.class)
    protected String center;

    @Column(nullable = true)
    protected Double radius;

    @Override
    public BaseObject copy() {
//        CirclePerimeter circlePerimeter = this.clone();
//        circlePerimeter.setKeyId(this.getKeyId().copy());
        CirclePerimeter circlePerimeter = (CirclePerimeter) super.copy();
        return circlePerimeter;
    }

    @Override
    public void set(BaseObject baseObject) {
        super.set(baseObject);
        CirclePerimeter circlePerimeter = (CirclePerimeter) baseObject;
        this.center = circlePerimeter.getCenter();
        this.radius = circlePerimeter.getRadius();
    }

    @Getter
    public String getCenter() {
        return center;
    }

    @Setter
    public void setCenter(String center) {
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
