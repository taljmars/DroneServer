package com.dronedb.scheme;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Value;

@Entity
@Table(name="missionitems")
public class Circle extends MissionItem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	

	@Value(value = "10.0")
	protected double radius;
	
	@Value(value = "1")
	protected int turns;
	
	protected double lat;
	protected double lng;
	
	public Circle() {
		super();
		type = MissionItemType.CIRCLE.toString();
	}
	
	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public int getTurns() {
		return turns;
	}

	public void setTurns(int turns) {
		this.turns = turns;
	}
	
	@Override
	public String toString() {
		return getClass().getCanonicalName() + " [objId=" + objId + "]"; 
	}
}
