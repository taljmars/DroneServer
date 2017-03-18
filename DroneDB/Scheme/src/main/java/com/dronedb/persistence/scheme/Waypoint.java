package com.dronedb.persistence.scheme;

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
public class Waypoint extends MissionItem implements Altitudable, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(nullable = true)
	protected double delay;
	
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
	}
	
	public double getDelay() {
		return delay;
	}

	public void setDelay(double delay) {
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
	public String toString() {
		return getClass().getCanonicalName() + " [objId=" + objId + "]"; 
	}

	@Override
	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	@Override
	public Double getAltitude() {
		return this.altitude;
	}
}
