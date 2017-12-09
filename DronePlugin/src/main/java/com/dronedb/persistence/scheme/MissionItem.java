package com.dronedb.persistence.scheme;

import com.db.persistence.scheme.BaseObject;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;

@XmlSeeAlso({
		Waypoint.class, LoiterTurns.class, ReturnToHome.class, Takeoff.class,
		RegionOfInterest.class, Land.class, SplineWaypoint.class, LoiterTime.class,
		LoiterUnlimited.class
})
@Entity
//@Inheritance(strategy=InheritanceType.JOINED)
//@DiscriminatorColumn(name="type")
@Table
public abstract class MissionItem extends BaseObject implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected Double lat;
	protected Double lon;

	public MissionItem() {}

	public MissionItem(MissionItem missionItem) {
		super(missionItem);
		this.lat = missionItem.getLat();
		this.lon = missionItem.getLon();
	}

	public abstract MissionItem clone();

	public void set(BaseObject baseObject) {
		MissionItem missionItem = (MissionItem) baseObject;
		this.lat = missionItem.getLat();
		this.lon = missionItem.getLon();
	}

	@Override
	public BaseObject copy() {
		MissionItem missionItem = (MissionItem) super.copy();
		return missionItem;
	}

	@Getter
	public Double getLat() {
		return lat;
	}

	@Setter
	public void setLat(Double lat) {
		this.lat = lat;
	}

	@Getter
	public Double getLon() {
		return lon;
	}

	@Setter
	public void setLon(Double lon) {
		this.lon = lon;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		MissionItem that = (MissionItem) o;

		if (!lat.equals(that.lat)) return false;
		return lon.equals(that.lon);
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + lat.hashCode();
		result = 31 * result + lon.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "MissionItem{" +
				super.toString() +
				", lat=" + lat +
				", lon=" + lon +
				'}';
	}
}
