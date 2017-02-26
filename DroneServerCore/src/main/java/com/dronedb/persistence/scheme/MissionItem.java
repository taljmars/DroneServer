package com.dronedb.persistence.scheme;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlSeeAlso;
import jdk.nashorn.internal.objects.annotations.Getter;

@XmlSeeAlso({Waypoint.class, Circle.class, ReturnToHome.class, Takeoff.class})
//@MappedSuperclass
@Entity
//@Inheritance(strategy=InheritanceType.JOINED)
//@DiscriminatorColumn(name="type")
//@Table(name="missionitems")
//@Table
public abstract class MissionItem extends BaseObject implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected String type;
	
	@Getter
	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return getClass().getCanonicalName() + " [objId=" + objId + "]";
	}
}
