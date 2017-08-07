package com.dronedb.persistence.scheme;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

//import org.hibernate.annotations.ColumnDefault;


@Entity
//@Table(name="missionitems")
//@NamedNativeQueries({
//    @NamedNativeQuery(
//            name    =   "getAllWaypoints",
//            query   =   "SELECT * FROM missionitems",
//                        resultClass = Waypoint.class
//    )
//})
@Sessionable
public class SplineWaypoint extends MissionItem implements Delayable, Serializable {

	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	protected Double delay;

	public SplineWaypoint() {
		super();
//		type = MissionItemType.WAYPOINT.toString();
		this.delay = 0.0;
	}

	public SplineWaypoint(SplineWaypoint waypoint) {
		super(waypoint);
		this.delay = waypoint.getDelay();
	}

	@Override
	public SplineWaypoint clone() {
		return new SplineWaypoint(this);
	}

	@Override
	public BaseObject copy() {
		SplineWaypoint waypoint = this.clone();
		waypoint.getKeyId().setObjId(this.getKeyId().getObjId());
		return waypoint;
	}

	@Override
	public void set(BaseObject baseObject) {
		super.set(baseObject);
		SplineWaypoint waypoint = (SplineWaypoint) baseObject;
		this.delay = waypoint.getDelay();
	}

	@Override
	public Double getDelay() {
		return delay;
	}

	@Override
	public void setDelay(Double delay) {
		this.delay = delay;
	}

	@Override
	public String toString() {
		return "SplineWaypoint{" +
				super.toString() +
				", delay=" + delay +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		SplineWaypoint that = (SplineWaypoint) o;

		return delay != null ? delay.equals(that.delay) : that.delay == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (delay != null ? delay.hashCode() : 0);
		return result;
	}
}
