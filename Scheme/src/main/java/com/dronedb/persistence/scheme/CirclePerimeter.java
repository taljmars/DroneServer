package com.dronedb.persistence.scheme;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

import static com.dronedb.persistence.scheme.Constants.CIRCLE_PERIMETER_QUERY_FROM_TIP_AND_PRIVATE;

/**
 * Created by taljmars on 3/19/17.
 */
@Entity
@Table
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "GetAllCirclePerimeters",
                query = "SELECT * FROM circleperimeter WHERE " + CIRCLE_PERIMETER_QUERY_FROM_TIP_AND_PRIVATE,
                resultClass = CirclePerimeter.class
        ),
        @NamedNativeQuery(
                name = "GetAllModifiedCirclePerimeters",
                query = "SELECT * FROM circleperimeter WHERE privatelyModified = true",
                resultClass = CirclePerimeter.class
        )
})
@Access(javax.persistence.AccessType.FIELD)
@Sessionable
public class CirclePerimeter extends Perimeter implements Serializable {

    @Column(nullable = true)
    @Basic(fetch=FetchType.EAGER)
    protected UUID center;

    @Column(nullable = true)
    protected Double radius;

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

    @Override
    public BaseObject copy() {
        CirclePerimeter circlePerimeter = this.clone();
        circlePerimeter.getKeyId().setObjId(this.getKeyId().getObjId());
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
