package com.dronedb.persistence.scheme;

import com.dronedb.persistence.triggers.*;
import com.dronedb.persistence.validations.NameNotEmpty;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
@UpdateTriggers({
	@UpdateTrigger(trigger = "com.dronedb.persistence.triggers.HandleRedundantMissionItemsTriggerImpl", phase = UpdateTrigger.PHASE.UPDATE),
})
@DeleteTriggers({
	@DeleteTrigger(trigger = "com.dronedb.persistence.triggers.HandleMissionDeletionTriggerImpl")
})
@NameNotEmpty
@Access(javax.persistence.AccessType.FIELD)
public class Mission extends BaseObject implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public Mission() {
		super();
		missionItemsUids = new ArrayList();
	}

	public Mission(Mission mission) {
		super(mission);
		this.name = mission.getName();
		this.defaultAlt = mission.getDefaultAlt();
		this.missionItemsUids = new ArrayList<>();
		for (UUID missionItemUid : mission.getMissionItemsUids()) {
			this.missionItemsUids.add(missionItemUid);
		}
	}

	@Override
	public void set(BaseObject baseObject) {
		System.out.println("Setting mission");
		Mission mission = (Mission) baseObject;
		this.name = mission.getName();
		this.defaultAlt = mission.getDefaultAlt();
		this.missionItemsUids = mission.getMissionItemsUids();
	}

	@Override
	@Transient
	public Mission clone() {
		return new Mission(this);
	}

	@Override
	public BaseObject copy() {
		Mission mission = this.clone();
		mission.objId = this.objId;
		return mission;
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
	
//	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
//	@JoinTable(name = "mission_missionitem", joinColumns = @JoinColumn(name = "mission_id", referencedColumnName = "objid"), inverseJoinColumns = @JoinColumn(name = "missionitem_id", referencedColumnName="objid"))
	@ElementCollection
	private List<UUID> missionItemsUids;
	
	@Getter
	public List<UUID> getMissionItemsUids() {
		return missionItemsUids;
	}
	
	@Setter
	public void setMissionItemsUids(List<UUID> missionItemsUids) {
		this.missionItemsUids.clear();
		if (missionItemsUids != null)
			this.missionItemsUids.addAll(missionItemsUids);
	}

	@Transient
	public void addMissionItemUid(UUID missionItemUid) {
		this.missionItemsUids.add(missionItemUid);
	}

	@Transient
	public boolean removeMissionItemUid(UUID missionItemUid) {
		return this.missionItemsUids.remove(missionItemUid);
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
		return missionItemsUids != null ? missionItemsUids.equals(mission.missionItemsUids) : mission.missionItemsUids == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		long temp;
		result = 31 * result + name.hashCode();
		result = 31 * result + (missionItemsUids != null ? missionItemsUids.hashCode() : 0);
		temp = Double.doubleToLongBits(defaultAlt);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "Mission{" +
				super.toString() +
				"name='" + name + '\'' +
				", missionItemsUids=" + missionItemsUids +
				", defaultAlt=" + defaultAlt +
				'}';
	}
}
