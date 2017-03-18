package com.dronedb.persistence.scheme;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
//@Table(name="missionitems")
public class Circle extends MissionItem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(nullable = true)
	protected double radius;
	
	@Column(nullable = true)
	protected int turns;
	
	public Circle() {
		super();
//		type = MissionItemType.CIRCLE.toString();
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
