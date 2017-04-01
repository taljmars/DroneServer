package com.dronedb.persistence.scheme;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
//@Table(name="missionitems")
public class Circle extends MissionItem implements Altitudable, Radiusable, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(nullable = true)
	protected double radius;

	@Column
	protected Double altitude;
	
	@Column(nullable = true)
	protected int turns;
	
	public Circle() {
		super();
	}

	public Circle(Circle circle) {
		super(circle);
		this.radius = circle.getRadius();
		this.altitude = circle.getAltitude();
		this.turns = circle.getTurns();
	}

	@Override
	public Circle clone() {
		return new Circle(this);
	}

	@Override
	public BaseObject copy() {
		Circle circle = this.clone();
		circle.objId = this.objId;
		return circle;
	}

	@Override
	public void set(BaseObject baseObject) {
		super.set(baseObject);
		Circle circle = (Circle) baseObject;
		this.radius = circle.getRadius();
		this.altitude = circle.getAltitude();
		this.turns = circle.getTurns();
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
