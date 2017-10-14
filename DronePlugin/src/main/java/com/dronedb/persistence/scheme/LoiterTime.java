package com.dronedb.persistence.scheme;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.Sessionable;
import com.db.persistence.triggers.UpdateTrigger;
import com.db.persistence.triggers.UpdateTriggers;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@UpdateTrigger(trigger = "com.db.persistence.triggers.DefaultValuesSetterTrigger", phase = UpdateTrigger.PHASE.PRE_PERSIST)
@Sessionable
public class LoiterTime extends MissionItem implements Altitudable, Serializable {

	private static final long serialVersionUID = 1L;

	protected Double altitude;

	protected Integer seconds;

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
//		LoiterTime loiterTime = this.clone();
//		loiterTime.setKeyId(this.getKeyId());
		LoiterTime loiterTime = (LoiterTime) super.copy();
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
	@Value(value = "10")
	@Column(nullable = false, name = "Alt")
	public Double getAltitude() {
		return this.altitude;
	}

	@Value(value = "5")
	@Column(nullable = false, name = "Sec")
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
