package com.dronedb.persistence.scheme.mission;

import com.dronedb.persistence.scheme.BaseObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
//@Table(name="missionitems")
public class Land extends MissionItem implements Altitudable, Serializable {

	private static final long serialVersionUID = 1L;

	@Column
	protected Double altitude;

	public Land() {
		super();
//		type = MissionItemType.LAND.toString();
	}

	public Land(Land land) {
		super(land);
		this.altitude = land.getAltitude();
	}

	@Override
	public Land clone() {
		return new Land(this);
	}

	@Override
	public BaseObject copy() {
		Land land = this.clone();
		land.objId = this.objId;
		return land;
	}

	@Override
	public void set(BaseObject baseObject) {
		super.set(baseObject);
		Land land = (Land) baseObject;
		this.altitude = land.getAltitude();
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
		return "Land{" +
				"altitude=" + altitude +
				'}';
	}

	@Override
	public void accept(ConvertDatabaseVisitor convertDatabaseVisitor) {
		convertDatabaseVisitor.visit(this);
	}
}
