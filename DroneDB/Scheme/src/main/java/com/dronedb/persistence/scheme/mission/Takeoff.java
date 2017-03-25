package com.dronedb.persistence.scheme;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

//import org.hibernate.annotations.ColumnDefault;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

@Entity
//@Table(name="missionitems")
public class Takeoff extends MissionItem implements Serializable {
		
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	protected double finishedAlt;
	
	public Takeoff() {
		super();
//		type = MissionItemType.TAKEOFF.toString();
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

	@Override
	public void accept(ConvertDatabaseVisitor convertDatabaseVisitor) {
		convertDatabaseVisitor.visit(this);
	}
}

