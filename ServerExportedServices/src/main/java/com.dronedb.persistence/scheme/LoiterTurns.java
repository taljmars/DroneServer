package com.dronedb.persistence.scheme;

public class LoiterTurns extends MissionItem implements Altitudable {

	protected Double altitude;

	protected Integer turns;
	
	public LoiterTurns() {
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

	public Integer getTurns() {
		return turns;
	}

	public void setTurns(Integer turns) {
		this.turns = turns;
	}

	@Override
	public String toString() {
		return "LoiterTurns{" +
				super.toString() +
				", altitude=" + altitude +
				", turns=" + turns +
				'}';
	}
}
