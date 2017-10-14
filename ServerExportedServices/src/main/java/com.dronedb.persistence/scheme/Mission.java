package com.dronedb.persistence.scheme;

import com.db.persistence.scheme.BaseObject;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import java.beans.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Mission extends BaseObject
{
	public Mission() {
//		super();
		missionItemsUids = new ArrayList();
	}

	protected String name;
	
	@Getter
	public String getName() {
		return name;
	}
	
	@Setter
	public void setName(String name) {
		this.name = name;
	}

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

	protected double defaultAlt;
	
	@Getter
	public double getDefaultAlt() {
		return defaultAlt;
	}
	
	@Setter
	public void setDefaultAlt(double alt) {
		defaultAlt = alt;
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
