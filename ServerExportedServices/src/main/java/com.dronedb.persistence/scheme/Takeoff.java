package com.dronedb.persistence.scheme;

public class Takeoff extends MissionItem {

	protected double finishedAlt;
	
	public Takeoff() {
//		super();
	}

	public double getFinishedAlt() {
		return finishedAlt;
	}

	public void setFinishedAlt(double finishedAlt) {
		this.finishedAlt = finishedAlt;
	}

	@Override
	public String toString() {
		return "Takeoff{" +
				"finishedAlt=" + finishedAlt +
				'}';
	}
}

