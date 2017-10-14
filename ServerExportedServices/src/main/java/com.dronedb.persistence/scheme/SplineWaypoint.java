package com.dronedb.persistence.scheme;

public class SplineWaypoint extends MissionItem implements Altitudable, Delayable {

	protected Double delay;

	private Double altitude;

	public SplineWaypoint() {
//		super();
//		this.delay = 0.0;
//		this.altitude = 0.0;
	}

	@Override
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
