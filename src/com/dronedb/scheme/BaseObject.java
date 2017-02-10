package com.dronedb.scheme;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import jdk.nashorn.internal.objects.annotations.Getter;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@XmlSeeAlso({Mission.class, MissionItem.class})
public abstract class BaseObject implements Serializable
{	
	private static final long serialVersionUID = 1L;  
	
	@Id
	@Basic(optional = false)  
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	@XmlElement(required = true)
	protected Integer objId;

	@Getter
	public Integer getObjId() {
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
    
	public <T extends BaseObject> void set(T baseObject) {
		this.objId = baseObject.objId;
	}
	
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
