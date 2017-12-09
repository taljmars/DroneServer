package com.dronedb.persistence.scheme;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.Sessionable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Sessionable
public class Waypoint extends MissionItem implements Delayable, Altitudable, Serializable {
	
	private static final long serialVersionUID = 1L;

	protected Double delay;

	protected double acceptanceRadius;

	protected double yawAngle;

	protected double orbitalRadius;

	protected boolean orbitCCW;

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
//		Waypoint waypoint = this.clone();
//		waypoint.getKeyId().setObjId(this.getKeyId().getObjId());
		Waypoint waypoint = (Waypoint) super.copy();
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
	@Column(nullable = true)
	public Double getDelay() {
		return delay;
	}

	@Override
	public void setDelay(Double delay) {
		this.delay = delay;
	}

	@Column(nullable = true, name = "acptRad")
	public double getAcceptanceRadius() {
		return acceptanceRadius;
	}

	public void setAcceptanceRadius(double acceptanceRadius) {
		this.acceptanceRadius = acceptanceRadius;
	}

	@Column(nullable = true)
	public double getYawAngle() {
		return yawAngle;
	}

	public void setYawAngle(double yawAngle) {
		this.yawAngle = yawAngle;
	}

	@Column(nullable = true, name = "orbitalRad")
	public double getOrbitalRadius() {
		return orbitalRadius;
	}

	public void setOrbitalRadius(double orbitalRadius) {
		this.orbitalRadius = orbitalRadius;
	}

	@Column(nullable = true)
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
	@Column(nullable = true, name = "Alt")
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
}
