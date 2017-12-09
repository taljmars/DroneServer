package com.dronedb.persistence.scheme;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

//import org.hibernate.annotations.ColumnDefault;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.Sessionable;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@Sessionable
public class Takeoff extends MissionItem implements Serializable {
		
	private static final long serialVersionUID = 1L;

	protected double finishedAlt;
	
	public Takeoff() {
		super();
//		type = MissionItemType.TAKEOFF.toString();
	}

	public Takeoff(Takeoff takeoff) {
		super(takeoff);
		this.finishedAlt = takeoff.getFinishedAlt();
	}

	@Override
	public Takeoff clone() {
		return new Takeoff(this);
	}

	@Override
	public BaseObject copy() {
//		Takeoff takeoff = this.clone();
//		takeoff.setKeyId(this.getKeyId());
		Takeoff takeoff = (Takeoff) super.copy();
		return takeoff;
	}

	@Override
	public void set(BaseObject baseObject) {
		super.set(baseObject);
		Takeoff takeoff = (Takeoff) baseObject;
		this.finishedAlt = takeoff.getFinishedAlt();
	}

	@Getter
	@Column(nullable = true)
	public double getFinishedAlt() {
		return finishedAlt;
	}

	@Setter
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

