package com.dronedb.persistence.scheme;

public class LoiterTime extends MissionItem implements Altitudable {

	protected Double altitude;

	protected Integer seconds;

	public LoiterTime() {
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

	public Integer getSeconds() {
		return seconds;
	}

	public void setSeconds(Integer seconds) {
		this.seconds = seconds;
	}

	@Override
	public String toString() {
		return "LoiterTime{" +
				super.toString() +
				", altitude=" + altitude +
				", seconds=" + seconds +
				'}';
	}
}
