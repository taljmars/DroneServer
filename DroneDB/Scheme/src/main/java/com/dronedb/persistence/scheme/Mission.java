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

import com.dronedb.triggers.DeleteTrigger;
import com.dronedb.triggers.DeleteTriggers;

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
	@DeleteTrigger(trigger = "com.dronedb.persistence.triggers.DeleteObjectTriggerImpl")
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

	public void addMissionItem(MissionItem missionItem) {
		if (this.missionItems == null)
			this.missionItems = new ArrayList<>();

		this.missionItems.add(missionItem);
	}

	public boolean removeMissionItem(MissionItem missionItem) {
		return this.missionItems.remove(missionItem);
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
	
	@PrePersist
	public void onCreate() {
		super.onCreate();
	}
	
	@PreUpdate
	public void onUpdate() {
		super.onUpdate();  
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		Mission mission = (Mission) o;

		if (Double.compare(mission.defaultAlt, defaultAlt) != 0) return false;
		if (!name.equals(mission.name)) return false;
		return missionItems != null ? missionItems.equals(mission.missionItems) : mission.missionItems == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		long temp;
		result = 31 * result + name.hashCode();
		result = 31 * result + (missionItems != null ? missionItems.hashCode() : 0);
		temp = Double.doubleToLongBits(defaultAlt);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "Mission{" +
				super.toString() +
				"name='" + name + '\'' +
				", missionItems=" + missionItems +
				", defaultAlt=" + defaultAlt +
				'}';
	}
}
