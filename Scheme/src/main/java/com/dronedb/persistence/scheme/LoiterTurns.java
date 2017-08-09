package com.dronedb.persistence.scheme;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
//@Table(name="missionitems")
@Sessionable
public class LoiterTurns extends MissionItem implements Altitudable, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column
	protected Double altitude;
	
	@Column(nullable = true)
	protected int turns;
	
	public LoiterTurns() {
		super();
	}

	public LoiterTurns(LoiterTurns circle) {
		super(circle);
		this.altitude = circle.getAltitude();
		this.turns = circle.getTurns();
	}

	@Override
	public LoiterTurns clone() {
		return new LoiterTurns(this);
	}

	@Override
	public BaseObject copy() {
		LoiterTurns circle = this.clone();
		circle.setKeyId(this.getKeyId());
		return circle;
	}

	@Override
	public void set(BaseObject baseObject) {
		super.set(baseObject);
		LoiterTurns circle = (LoiterTurns) baseObject;
		this.altitude = circle.getAltitude();
		this.turns = circle.getTurns();
	}

	@Override
	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	@Override
	public Double getAltitude() {
		return this.altitude;
	}

	public int getTurns() {
		return turns;
	}

	public void setTurns(int turns) {
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
