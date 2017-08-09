package com.dronedb.persistence.scheme;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
//@Table(name="missionitems")
@Sessionable
public class LoiterTime extends MissionItem implements Altitudable, Serializable {

	private static final long serialVersionUID = 1L;

	@Column
	protected Double altitude;

	@Column(nullable = true)
	protected int seconds;

	public LoiterTime() {
		super();
	}

	public LoiterTime(LoiterTime loiterTime) {
		super(loiterTime);
		this.altitude = loiterTime.getAltitude();
		this.seconds = loiterTime.getSeconds();
	}

	@Override
	public LoiterTime clone() {
		return new LoiterTime(this);
	}

	@Override
	public BaseObject copy() {
		LoiterTime loiterTime = this.clone();
		loiterTime.setKeyId(this.getKeyId());
		return loiterTime;
	}

	@Override
	public void set(BaseObject baseObject) {
		super.set(baseObject);
		LoiterTime loiterTime = (LoiterTime) baseObject;
		this.altitude = loiterTime.getAltitude();
		this.seconds = loiterTime.getSeconds();
	}

	@Override
	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	@Override
	public Double getAltitude() {
		return this.altitude;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
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
