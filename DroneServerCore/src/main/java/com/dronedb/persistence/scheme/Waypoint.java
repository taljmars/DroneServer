package com.dronedb.persistence.scheme;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import com.dronedb.persistence.triggers.UpdateTrigger;
import com.dronedb.persistence.triggers.UpdateTrigger.PHASE;

@Entity
//@Table(name="missionitems")
//@NamedNativeQueries({
//    @NamedNativeQuery(
//            name    =   "getAllWaypoints",
//            query   =   "SELECT * FROM missionitems",
//                        resultClass = Waypoint.class
//    )
//})
public class Waypoint extends MissionItem implements Serializable {
	
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
	protected double lat;
	
	@Column(nullable = true)
	protected double lng;
	
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
}
