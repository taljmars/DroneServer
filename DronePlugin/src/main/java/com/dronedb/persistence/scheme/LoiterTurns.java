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
public class LoiterTurns extends MissionItem implements Altitudable, Serializable {
	
	private static final long serialVersionUID = 1L;

	protected Double altitude;
	
	@Column(nullable = true)
	protected Integer turns;
	
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
//		LoiterTurns circle = this.clone();
//		circle.setKeyId(this.getKeyId());
		LoiterTurns circle = (LoiterTurns) super.copy();
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
	@Value(value = "10")
	@Column(nullable = true, name = "Alt")
	public Double getAltitude() {
		return this.altitude;
	}

	@Value(value = "3")
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
