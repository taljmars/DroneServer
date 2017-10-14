package com.dronedb.persistence.scheme;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.Sessionable;
import com.db.persistence.triggers.UpdateTrigger;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@UpdateTrigger(trigger = "com.db.persistence.triggers.DefaultValuesSetterTrigger", phase = UpdateTrigger.PHASE.PRE_PERSIST)
@Sessionable
public class LoiterUnlimited extends MissionItem implements Altitudable, Serializable {

	private static final long serialVersionUID = 1L;

	protected Double altitude;

	public LoiterUnlimited() {
		super();
	}

	public LoiterUnlimited(LoiterUnlimited loiterUnlimited) {
		super(loiterUnlimited);
		this.altitude = loiterUnlimited.getAltitude();
	}

	@Override
	public LoiterUnlimited clone() {
		return new LoiterUnlimited(this);
	}

	@Override
	public BaseObject copy() {
//		LoiterUnlimited loiterUnlimited = this.clone();
//		loiterUnlimited.setKeyId(this.getKeyId());
		LoiterUnlimited loiterUnlimited = (LoiterUnlimited) super.copy();
		return loiterUnlimited;
	}

	@Override
	public void set(BaseObject baseObject) {
		super.set(baseObject);
		LoiterUnlimited loiterUnlimited = (LoiterUnlimited) baseObject;
		this.altitude = loiterUnlimited.getAltitude();
	}

	@Override
	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	@Override
	@Value(value = "10")
	@Column(nullable = true, name = "Alt")
	public Double getAltitude() {
		return this.altitude;
	}

	@Override
	public String toString() {
		return "LoiterUnlimited{" +
				super.toString() +
				", altitude=" + altitude +
				'}';
	}
}
