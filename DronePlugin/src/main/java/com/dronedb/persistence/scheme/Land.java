package com.dronedb.persistence.scheme;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.Sessionable;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.awt.*;
import java.io.Serializable;

@Entity
//@Table(name="missionitems")
@Sessionable
public class Land extends MissionItem implements Altitudable, Serializable {

	private static final long serialVersionUID = 1L;

	protected Double altitude;

	public Land() {
		super();
//		type = MissionItemType.LAND.toString();
		this.altitude = 0.0;
	}

	public Land(Land land) {
		super(land);
		this.altitude = land.getAltitude();
	}

	@Override
	public Land clone() {
		return new Land(this);
	}

	@Override
	public BaseObject copy() {
//		Land land = this.clone();
//		land.setKeyId(this.getKeyId());
		Land land = (Land) super.copy();
		return land;
	}

	@Override
	public void set(BaseObject baseObject) {
		super.set(baseObject);
		Land land = (Land) baseObject;
		this.altitude = land.getAltitude();
	}

	@Override
	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	@Override
//	@Column(nullable = false, name = "Alt")
	@Column(nullable = true, name = "Alt")
	public Double getAltitude() {
		return this.altitude;
	}

	@Override
	public String toString() {
		return "Land{" +
				super.toString() +
				", altitude=" + altitude +
				'}';
	}

}
