package com.dronedb.persistence.scheme;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.dronedb.persistence.scheme.mission.Mission;
import com.dronedb.persistence.scheme.mission.MissionItem;
import com.dronedb.persistence.scheme.perimeter.Perimeter;
import com.dronedb.persistence.scheme.perimeter.Point;
import jdk.nashorn.internal.objects.annotations.Getter;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@XmlSeeAlso({Mission.class, MissionItem.class, Perimeter.class, Point.class})
@MappedSuperclass
public abstract class BaseObject implements Serializable
{	
	private static final long serialVersionUID = 1L;
	
	public BaseObject() {
		this.objId = UUID.randomUUID();
	}

	public BaseObject(BaseObject baseObject) {
		this.objId = UUID.randomUUID();
	}

	@Transient
	public abstract BaseObject clone();

	@Transient
	public abstract BaseObject copy();
	
	@Id
	@Basic(optional = false)
	@XmlElement(required = true)
	protected UUID objId;

	@Getter
	public UUID getObjId() {
		return objId;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@XmlElement(required = true)
	private Date createdAt;

	@PrePersist  
	public void onCreate() {  
		this.createdAt = new Date();
	}
    
	@Getter
	public Date getCreationDate() {  
		return createdAt;  
	}
  
	@Temporal(TemporalType.TIMESTAMP)
	@XmlElement(required = true)
	private Date updatedAt;
    
	@PreUpdate
	public void onUpdate() {
		this.updatedAt = new Date();
	}
    
	@Getter
	public Date getChangeDate() {  
		return updatedAt;
	} 

	@Version
	@XmlElement(required = true)
	protected Long version;
    
	@Getter
	public Long getVersion() {
		return version;
	}
    
	public abstract void set(BaseObject baseObject);

	@Override  
	public int hashCode() {  
		int hash = 0;  
		hash += (this.getObjId() != null ? this.getObjId().hashCode() : 0);  
  
		return hash;  
	}  
  
	@Override  
	public boolean equals(Object object) {  
		if (this == object)  
			return true;  
		if (object == null)  
			return false;  
		if (getClass() != object.getClass())  
			return false;  
  
		BaseObject other = (BaseObject) object;  
		if (this.getObjId() != other.getObjId() && (this.getObjId() == null || !this.objId.equals(other.objId))) {  
			return false;  
		}  
		return true;  
	}  
    
	@Override  
	public String toString() {  
		return this.getClass().getCanonicalName() + " [objId=" + objId + "]";  
	}
}
