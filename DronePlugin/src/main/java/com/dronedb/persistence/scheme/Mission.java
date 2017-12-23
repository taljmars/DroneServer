package com.dronedb.persistence.scheme;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.Sessionable;
import com.db.persistence.scheme.TargetType;
import com.db.persistence.triggers.DeleteTrigger;
import com.db.persistence.triggers.UpdateTrigger;
import com.dronedb.persistence.validations.NameNotEmptyValidation;
import com.dronedb.persistence.validations.NoPostLandOrRTLItemsValidation;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.db.persistence.scheme.Constants.GEN_CTX;


@NamedNativeQueries({
	@NamedNativeQuery(
		name = "GetAllMissions",
//		query = "SELECT * FROM mission WHERE " + MISSION_QUERY_FROM_TIP_AND_PRIVATE,
		query = "SELECT * FROM Mission WHERE " + GEN_CTX,
		resultClass = Mission.class
	),
    @NamedNativeQuery(
    	name = "GetMissionById",
//    	query = "SELECT * FROM mission WHERE objid=:OBJID AND " + MISSION_QUERY_FROM_TIP_AND_PRIVATE,
		query = "SELECT * FROM Mission WHERE (objid = :OBJID) AND " + GEN_CTX,
    	resultClass = Mission.class
    ),
	@NamedNativeQuery(
		name = "GetMissionByName",
		//query = "SELECT * FROM mission WHERE name ilike =:NAME AND " + MISSION_QUERY_FROM_TIP_AND_PRIVATE,
		query = "SELECT * FROM Mission WHERE " + GEN_CTX + " AND (name ilike = :NAME)",
		resultClass = Mission.class
    ),
	@NamedNativeQuery(
			name = "GetAllModifiedMissions",
			query = "SELECT * FROM Mission WHERE " + GEN_CTX + " AND (entityManagerCtx != 0)",
			resultClass = Mission.class
	)
})
@Entity
@Table

@UpdateTrigger(trigger = "com.dronedb.persistence.triggers.HandleRedundantMissionItemsTrigger", phase = UpdateTrigger.PHASE.UPDATE)
@DeleteTrigger(trigger = "com.dronedb.persistence.triggers.HandleMissionDeletionTrigger")

@NameNotEmptyValidation
@NoPostLandOrRTLItemsValidation
@Access(javax.persistence.AccessType.FIELD)
@Sessionable
@XmlRootElement(namespace = "com.db.persistence.scheme")
public class Mission extends BaseObject implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public Mission() {
		super();
		missionItemsUids = new ArrayList<>();
	}

	public Mission(Mission mission) {
		super(mission);
		this.name = mission.getName();
		this.defaultAlt = mission.getDefaultAlt();
		this.missionItemsUids = new ArrayList<>();
		for (String missionItemUid : mission.getMissionItemsUids()) {
			this.missionItemsUids.add(missionItemUid);
		}
	}

	@Override
	public void set(BaseObject baseObject) {
		System.out.println("Setting mission");
		Mission mission = (Mission) baseObject;
		this.name = mission.getName();
		this.defaultAlt = mission.getDefaultAlt();
		this.missionItemsUids = new ArrayList<>();
		for (String missionItemUid : mission.getMissionItemsUids()) {
			this.missionItemsUids.add(missionItemUid);
		}
	}

	/**
	 * Clone will generate new Objid for the new object
 	 */
	@Override
	@Transient
	public Mission clone() {
		return new Mission(this);
	}

	/**
	 * Copy will do the same as clone but with the same objid
	 */
	@Override
	public BaseObject copy() {
//		Mission mission = this.clone();
//		mission.setKeyId(this.getKeyId().copy());
		Mission mission = (Mission) super.copy();
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
	
	//@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
//	@JoinTable(name = "mission_missionitem", joinColumns = @JoinColumn(name = "mission_id", referencedColumnName = "objid"), inverseJoinColumns = @JoinColumn(name = "missionitem_id", referencedColumnName="objid"))
	@ElementCollection(fetch = FetchType.EAGER)
	@TargetType(clz = MissionItem.class)
	private List<String> missionItemsUids;
	
	@Getter
	public List<String> getMissionItemsUids() {
		return missionItemsUids;
	}
	
	@Setter
	public void setMissionItemsUids(List<String> missionItemsUids) {
		this.missionItemsUids.clear();
		if (missionItemsUids != null)
			this.missionItemsUids.addAll(missionItemsUids);
	}

	@Transient
	public void addMissionItemUid(String missionItemUid) {
		this.missionItemsUids.add(missionItemUid);
	}

	@Transient
	public boolean removeMissionItemUid(String missionItemUid) {
		return this.missionItemsUids.remove(missionItemUid);
	}

	@Value(value = "20.0")
	@Column(nullable = false)
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
