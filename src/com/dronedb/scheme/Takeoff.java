package com.dronedb.scheme;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Value;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

@Entity
@Table(name="missionitems")
public class Takeoff extends MissionItem implements Serializable {
		
	private static final long serialVersionUID = 1L;

	@Value(value = "10.0")
	protected double finishedAlt = 10;
	
	public Takeoff() {
		super();
		type = MissionItemType.TAKEOFF.toString();
	}
	
	@Getter
	public double getFinishedAlt() {
		return finishedAlt;
	}

	@Setter
	public void setFinishedAlt(double finishedAlt) {
		this.finishedAlt = finishedAlt;
	}
	
	@Override
	public String toString() {
		return getClass().getCanonicalName() + " [objId=" + objId + "]"; 
	}
}

