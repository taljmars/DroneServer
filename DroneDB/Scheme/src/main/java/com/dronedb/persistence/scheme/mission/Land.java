package com.dronedb.persistence.scheme;

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
