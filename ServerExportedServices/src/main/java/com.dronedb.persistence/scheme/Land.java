package com.dronedb.persistence.scheme;

public class Land extends MissionItem implements Altitudable {

	protected Double altitude;

	public Land() {
//		super();
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

}
