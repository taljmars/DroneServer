package com.dronedb.persistence.scheme;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
//@Table(name="missionitems")
@Sessionable
public class LoiterUnlimited extends MissionItem implements Altitudable, Radiusable, Serializable {

	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	protected double radius;

	@Column
	protected Double altitude;

	public LoiterUnlimited() {
		super();
	}

	public LoiterUnlimited(LoiterUnlimited loiterUnlimited) {
		super(loiterUnlimited);
		this.radius = loiterUnlimited.getRadius();
		this.altitude = loiterUnlimited.getAltitude();
	}

	@Override
	public LoiterUnlimited clone() {
		return new LoiterUnlimited(this);
	}

	@Override
	public BaseObject copy() {
		LoiterUnlimited loiterUnlimited = this.clone();
		loiterUnlimited.setKeyId(this.getKeyId());
		return loiterUnlimited;
	}

	@Override
	public void set(BaseObject baseObject) {
		super.set(baseObject);
		LoiterUnlimited loiterUnlimited = (LoiterUnlimited) baseObject;
		this.radius = loiterUnlimited.getRadius();
		this.altitude = loiterUnlimited.getAltitude();
	}

	@Override
	public Double getRadius() {
		return radius;
	}

	@Override
	public void setRadius(Double radius) {
		this.radius = radius;
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
				", radius=" + radius +
				", altitude=" + altitude +
				'}';
	}
}
