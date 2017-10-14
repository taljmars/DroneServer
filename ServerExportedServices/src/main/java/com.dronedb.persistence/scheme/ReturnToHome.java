package com.dronedb.persistence.scheme;

public class ReturnToHome extends MissionItem implements Altitudable {

	protected Double altitude;
	
	public ReturnToHome() {
		super();
	}

	public ReturnToHome(ReturnToHome returnToHome) {
//		super(returnToHome);
//		this.altitude = returnToHome.getAltitude();
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

