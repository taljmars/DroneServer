package com.dronedb.persistence.scheme;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

//import org.hibernate.annotations.ColumnDefault;


@Entity
//@Table(name="missionitems")
public class ReturnToHome extends MissionItem implements Altitudable, Serializable {
		
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	protected Double altitude;
	
	public ReturnToHome() {
		super();
//		type = MissionItemType.RTL.toString();
	}

	@Override
	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	@Override
	public Double getAltitude() {
		return this.altitude;
	}

	@Override
	public void accept(ConvertDatabaseVisitor convertDatabaseVisitor) {
		convertDatabaseVisitor.visit(this);
	}
}

