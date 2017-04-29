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

	public ReturnToHome(ReturnToHome returnToHome) {
		super(returnToHome);
		this.altitude = returnToHome.getAltitude();
	}

	@Override
	public ReturnToHome clone() {
		return new ReturnToHome(this);
	}

	@Override
	public BaseObject copy() {
		ReturnToHome returnToHome = this.clone();
		returnToHome.setKeyId(this.getKeyId());
		return returnToHome;
	}


	@Override
	public void set(BaseObject baseObject) {
		super.set(baseObject);
		ReturnToHome returnToHome = (ReturnToHome) baseObject;
		this.altitude = returnToHome.getAltitude();
	}

	@Override
	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	@Override
	public Double getAltitude() {
		return this.altitude;
	}
}

