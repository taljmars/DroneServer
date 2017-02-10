package com.dronedb.scheme;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Value;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

@Entity
@Table
public class Mission extends BaseObject implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Value(value = "20.0")
	protected double defaultAlt;
	
	@Getter
	public double getDefaultAlt() {
		return defaultAlt;
	}
	
	@Setter
	public void setDefaultAlt(double alt) {
		defaultAlt = alt;
	}
	
	protected List<MissionItem> items;
	
	@Getter
	public List<MissionItem> getItems() {
		return items;
	}
	
	@Setter
	public void getItems(List<MissionItem> items) {
		if (this.items == null || this.items.isEmpty())
			this.items = new ArrayList<>();
		this.items.clear();
		this.items.addAll(items);
	}
	
	public void addMissionItem(MissionItem missionItem) {
		if (this.items == null || this.items.isEmpty())
			this.items = new ArrayList<>();
		
		this.items.add(missionItem);
	}
	
	public void removeMissionItem(MissionItem missionItem) {
		if (this.items == null || this.items.isEmpty())
			this.items = new ArrayList<>();
		
		this.items.remove(missionItem);
	}
	
	public <T extends BaseObject> void set(T mission) {
		super.set(mission);
		Mission missionCast = (Mission) mission;
		this.setDefaultAlt(missionCast.defaultAlt);
		this.items = missionCast.getItems();
	}
	
	@Override  
    public int hashCode() {  
        int hash = 0;  
        hash += (this.getObjId() != null ? this.getObjId().hashCode() : 0);
        hash += (this.getItems() != null ? this.getItems().hashCode() : 0);
  
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
        
        if (!this.getItems().equals(other.getItems()))
        	return false;
        
        return true;  
    }  
	
	@Override
	public String toString() {
		return "Mission [objid=" + objId + ", defaultAlt=" + defaultAlt + ", items=" + items + "]";
	}
}
