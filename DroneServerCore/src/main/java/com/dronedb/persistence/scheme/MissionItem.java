package com.dronedb.persistence.scheme;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

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

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "mission_id", referencedColumnName="objid", nullable = false)
//	private Mission mission;
//	
//	@XmlInverseReference(mappedBy = "missionItems")
//	public Mission getMission() {
//		return this.mission;
//	}
//
//	public void setMission(Mission mission) {
//		this.mission = mission;
//	}

//	
//	private Integer itemOrder;
//	
//	@Setter
//	public void setItemOrder(Integer itemOrder) {
//		this.itemOrder = itemOrder;
//	}
//	
//	@Getter
//	public Integer getItemOrder() {
//		return itemOrder;
//	}
	
	@Override
	public String toString() {
		return getClass().getCanonicalName() + " [objId=" + objId + "]";
	}
}
