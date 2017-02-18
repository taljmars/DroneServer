package com.dronedb.scheme;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Value;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

@Entity
@Table(name="missionitems")
public class ReturnToHome extends MissionItem implements Serializable {
		
	private static final long serialVersionUID = 1L;

	@Value(value = "0.0")
	protected double returnAltitude;
	
	public ReturnToHome() {
		super();
		type = MissionItemType.RTL.toString();
	}
	
	@Getter
	public double getReturnAltitude() {
		return returnAltitude;
	}

	@Setter
	public void setReturnAltitude(double returnAltitude) {
		this.returnAltitude = returnAltitude;
	}
	
	@Override
	public String toString() {
		return getClass().getCanonicalName() + " [objId=" + objId + "]"; 
	}
}

