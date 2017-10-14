package com.dronedb.persistence.scheme;

public class LoiterUnlimited extends MissionItem implements Altitudable {

	protected Double altitude;

	public LoiterUnlimited() {
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
		return "LoiterUnlimited{" +
				super.toString() +
				", altitude=" + altitude +
				'}';
	}
}
