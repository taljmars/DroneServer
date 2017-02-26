package com.dronedb.persistence.scheme;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.dronedb.persistence.triggers.DeleteTrigger;
import com.dronedb.persistence.triggers.DeleteTriggers;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

@NamedNativeQueries({
	@NamedNativeQuery(
		name = "GetAllMissions",
		query = "select * from missions",
		resultClass = Mission.class
	),
    @NamedNativeQuery(
    	name = "GetMissionById",
    	query = "SELECT * FROM missions where objid=:OBJID",
    	resultClass = Mission.class
    ),
	@NamedNativeQuery(
		name = "GetMissionByName",
		query = "SELECT * FROM missions where name ilike =:NAME",
		resultClass = Mission.class
    )
})
@Entity
@Table(name="missions")
@DeleteTriggers({
	@DeleteTrigger(trigger = "com.dronedb.persistence.triggers.internal.DeleteObjectTriggerImpl")
})
@Access(javax.persistence.AccessType.FIELD)
public class Mission extends BaseObject implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public Mission() {
		super();
		missionItems = new ArrayList();
	}
	
	@Column(nullable = true)
	protected String name;
	
	@Getter
	public String getName() {
		return name;
	}
	
	@Setter
	public void setName(String name) {
		this.name = name;
	}
	
	@OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinTable(name = "mission_missionitem", joinColumns = @JoinColumn(name = "mission_id", referencedColumnName = "objid"), inverseJoinColumns = @JoinColumn(name = "missionitem_id", referencedColumnName="objid"))
	private List<MissionItem> missionItems;
	
	@Getter
	public List<MissionItem> getMissionItems() {
		return missionItems;
	}
	
	@Setter 
	public void setMissionItems(List<MissionItem> missionItems) {
		this.missionItems.addAll(missionItems);
	}
	
	@Column(nullable = true)
	protected double defaultAlt;
	
	@Getter
	public double getDefaultAlt() {
		return defaultAlt;
	}
	
	@Setter
	public void setDefaultAlt(double alt) {
		defaultAlt = alt;
	}
	
	public <T extends BaseObject> void set(T mission) {
		super.set(mission);
		Mission missionCast = (Mission) mission;
		this.setDefaultAlt(missionCast.defaultAlt);
	}
	
	@Override  
	public int hashCode() {  
		int hash = 0;  
		hash += (this.getObjId() != null ? this.getObjId().hashCode() : 0);
		return hash;  
	} 
	
	@PrePersist
	public void onCreate() {
		super.onCreate();
	}
	
	@PreUpdate
	public void onUpdate() {
		super.onUpdate();  
	}
  
	@Override  
	public boolean equals(Object object) {  
		if (this == object)  
			return true;  
		if (object == null)  
			return false;  
		if (getClass() != object.getClass())  
			return false;  
  
		Mission other = (Mission) object;
		if (this.getObjId() != other.getObjId() && (this.getObjId() == null || !this.objId.equals(other.objId)))  
			return false;  
        
		if (this.getDefaultAlt() != other.getDefaultAlt())
			return false;
        
		return true;  
	}  
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " [objId=" + objId + ", missionItems=" + missionItems + ", defaultAlt=" + defaultAlt + "]";
	}
}
