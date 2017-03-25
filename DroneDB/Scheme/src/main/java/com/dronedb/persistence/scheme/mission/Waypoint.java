package com.dronedb.persistence.scheme.mission;

import com.dronedb.persistence.scheme.BaseObject;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

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
public class Waypoint extends MissionItem implements Delayable, Altitudable, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(nullable = true)
	protected Double delay;
	
	@Column(nullable = true)
	protected double acceptanceRadius;
	
	@Column(nullable = true)
	protected double yawAngle;

	@Column(nullable = true)
	protected double orbitalRadius;

	@Column(nullable = true)
	protected boolean orbitCCW;

	@Column(nullable = true)
	private Double altitude;

	public Waypoint() {
		super();
//		type = MissionItemType.WAYPOINT.toString();
		this.delay = 0.0;
		this.altitude = 0.0;
	}

	public Waypoint(Waypoint waypoint) {
		super(waypoint);
		this.altitude = waypoint.getAltitude();
		this.delay = waypoint.getDelay();
		this.acceptanceRadius = waypoint.getAcceptanceRadius();
		this.orbitalRadius = waypoint.getOrbitalRadius();
		this.yawAngle = waypoint.getYawAngle();
	}

	@Override
	public Waypoint clone() {
		return new Waypoint(this);
	}

	@Override
	public BaseObject copy() {
		Waypoint waypoint = this.clone();
		waypoint.objId = this.objId;
		return waypoint;
	}

	@Override
	public void set(BaseObject baseObject) {
		super.set(baseObject);
		Waypoint waypoint = (Waypoint) baseObject;
		this.altitude = waypoint.getAltitude();
		this.delay = waypoint.getDelay();
		this.acceptanceRadius = waypoint.getAcceptanceRadius();
		this.orbitalRadius = waypoint.getOrbitalRadius();
		this.yawAngle = waypoint.getYawAngle();
	}

	@Override
	public Double getDelay() {
		return delay;
	}

	@Override
	public void setDelay(Double delay) {
		this.delay = delay;
	}

	public double getAcceptanceRadius() {
		return acceptanceRadius;
	}

	public void setAcceptanceRadius(double acceptanceRadius) {
		this.acceptanceRadius = acceptanceRadius;
	}

	public double getYawAngle() {
		return yawAngle;
	}

	public void setYawAngle(double yawAngle) {
		this.yawAngle = yawAngle;
	}

	public double getOrbitalRadius() {
		return orbitalRadius;
	}

	public void setOrbitalRadius(double orbitalRadius) {
		this.orbitalRadius = orbitalRadius;
	}

	public boolean isOrbitCCW() {
		return orbitCCW;
	}

	public void setOrbitCCW(boolean orbitCCW) {
		this.orbitCCW = orbitCCW;
	}

	@Override
	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	@Override
	public Double getAltitude() {
		return this.altitude;
	}

	@Override
	public String toString() {
		return "Waypoint{" +
				super.toString() +
				"delay=" + delay +
				", acceptanceRadius=" + acceptanceRadius +
				", yawAngle=" + yawAngle +
				", orbitalRadius=" + orbitalRadius +
				", orbitCCW=" + orbitCCW +
				", altitude=" + altitude +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		Waypoint waypoint = (Waypoint) o;

		if (Double.compare(waypoint.acceptanceRadius, acceptanceRadius) != 0) return false;
		if (Double.compare(waypoint.yawAngle, yawAngle) != 0) return false;
		if (Double.compare(waypoint.orbitalRadius, orbitalRadius) != 0) return false;
		if (orbitCCW != waypoint.orbitCCW) return false;
		if (delay != null ? !delay.equals(waypoint.delay) : waypoint.delay != null) return false;
		return altitude != null ? altitude.equals(waypoint.altitude) : waypoint.altitude == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		long temp;
		result = 31 * result + (delay != null ? delay.hashCode() : 0);
		temp = Double.doubleToLongBits(acceptanceRadius);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(yawAngle);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(orbitalRadius);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		result = 31 * result + (orbitCCW ? 1 : 0);
		result = 31 * result + (altitude != null ? altitude.hashCode() : 0);
		return result;
	}

	@Override
	public void accept(ConvertDatabaseVisitor convertDatabaseVisitor) {
		convertDatabaseVisitor.visit(this);
	}
}
