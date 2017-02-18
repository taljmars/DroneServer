package com.dronedb.scheme;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlSeeAlso;

//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@XmlSeeAlso({Waypoint.class, Circle.class, ReturnToHome.class, Takeoff.class})
@MappedSuperclass
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="type")
@Table(name="missionitems")
public abstract class MissionItem extends BaseObject implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected String type;
	

	public String getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return getClass().getCanonicalName() + " [objId=" + objId + "]";
	}
}
