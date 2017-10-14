package com.dronedb.persistence.scheme;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.Sessionable;

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
public class SplineWaypoint extends MissionItem implements Altitudable, Delayable, Serializable {

	private static final long serialVersionUID = 1L;

	protected Double delay;

	private Double altitude;

	public SplineWaypoint() {
		super();
//		type = MissionItemType.WAYPOINT.toString();
		this.delay = 0.0;
		this.altitude = 0.0;
	}

	public SplineWaypoint(SplineWaypoint waypoint) {
		super(waypoint);
		this.delay = waypoint.getDelay();
		this.altitude = waypoint.getAltitude();
	}

	@Override
	public SplineWaypoint clone() {
		return new SplineWaypoint(this);
	}

	@Override
	public BaseObject copy() {
//		SplineWaypoint waypoint = this.clone();
//		waypoint.getKeyId().setObjId(this.getKeyId().getObjId());
		SplineWaypoint waypoint = (SplineWaypoint) super.copy();
		return waypoint;
	}

	@Override
	public void set(BaseObject baseObject) {
		super.set(baseObject);
		SplineWaypoint waypoint = (SplineWaypoint) baseObject;
		this.delay = waypoint.getDelay();
		this.altitude = waypoint.getAltitude();
	}

	@Override
	@Column(nullable = true)
	public Double getDelay() {
		return delay;
	}

	@Override
	public void setDelay(Double delay) {
		this.delay = delay;
	}

	@Override
	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	@Override
	@Column(nullable = true, name = "Alt")
	public Double getAltitude() {
		return this.altitude;
	}

	@Override
	public String toString() {
		return "SplineWaypoint{" +
				", " +super.toString() +
				", delay=" + delay +
				", altitude=" + altitude +
				'}';
	}
}
