package com.dronedb.persistence.scheme;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.Sessionable;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

//import org.hibernate.annotations.ColumnDefault;


@Entity
//@Table(name="missionitems")
@Sessionable
public class ReturnToHome extends MissionItem implements Altitudable, Serializable {
		
	private static final long serialVersionUID = 1L;

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
//		ReturnToHome returnToHome = this.clone();
//		returnToHome.setKeyId(this.getKeyId());
		ReturnToHome returnToHome = (ReturnToHome) super.copy();
		return returnToHome;
	}


	@Override
	public void set(BaseObject baseObject) {
		super.set(baseObject);
		ReturnToHome returnToHome = (ReturnToHome) baseObject;
		this.altitude = returnToHome.getAltitude();
	}

	@Override
	@Column(nullable = true, name = "Alt")
	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}

	@Override
	public Double getAltitude() {
		return this.altitude;
	}
}

