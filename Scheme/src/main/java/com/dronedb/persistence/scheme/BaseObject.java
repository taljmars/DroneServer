package com.dronedb.persistence.scheme;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.springframework.beans.factory.annotation.Value;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@XmlSeeAlso({Mission.class, MissionItem.class, Perimeter.class, Point.class})
@XmlSeeAlso({Mission.class, MissionItem.class, Point.class})
@MappedSuperclass
public abstract class BaseObject implements Serializable
{	
	private static final long serialVersionUID = 1L;

	public BaseObject() {
		this.keyId = new KeyId();
	}

	public BaseObject(BaseObject baseObject) {
		keyId = new KeyId();
	}

	/**
	 * Make a clone of the object (new UUID)
	 * @return
	 */
	@Transient
	public abstract BaseObject clone();

	/**
	 * Make a copy of the object, (same UUID)
	 * @return
	 */
	@Transient
	public abstract BaseObject copy();

	private KeyId keyId;

	@EmbeddedId
	public KeyId getKeyId() {
		return keyId;
	}

	public void setKeyId(KeyId keyId) {
		this.keyId = keyId;
	}

	private Date creationDate;

	@PrePersist  
	public void onCreate() {  
		this.creationDate = new Date();
	}

	@XmlTransient
	@Temporal(TemporalType.TIMESTAMP)
	@Getter
	public Date getCreationDate() {  
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	private Date updatedAt;
    
	@PreUpdate
	public void onUpdate() {
		this.updatedAt = new Date();
	}

	@XmlTransient
	@Temporal(TemporalType.TIMESTAMP)
	@Getter
	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

//	protected Long version;
//
//	@XmlTransient
//	@Version
//	@Getter
//	public Long getVersion() {
//		return version;
//	}
//
//	public void setVersion(Long version) {
//		this.version = version;
//	}

	protected boolean deleted;

	@Getter
	public boolean isDeleted() {
		return deleted;
	}

	@XmlTransient
	@Value("false")
	@Setter
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	protected int fromRevision;

	@Getter
	public int getFromRevision() {
		return fromRevision;
	}

	@XmlTransient
	@Setter
	public void setFromRevision(int fromRevision) {
		this.fromRevision = fromRevision;
	}

	public abstract void set(BaseObject baseObject);

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		BaseObject that = (BaseObject) o;

		if (deleted != that.deleted) return false;
		if (fromRevision != that.fromRevision) return false;
		if (keyId != null ? !keyId.equals(that.keyId) : that.keyId != null) return false;
		if (creationDate != null ? !creationDate.equals(that.creationDate) : that.creationDate != null) return false;
		return updatedAt != null ? updatedAt.equals(that.updatedAt) : that.updatedAt == null;
	}

	@Override
	public int hashCode() {
		int result = keyId != null ? keyId.hashCode() : 0;
		result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
		result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
		result = 31 * result + (deleted ? 1 : 0);
		result = 31 * result + fromRevision;
		return result;
	}

	@Override
	public String toString() {
		return "BaseObject{" +
				"keyId=" + keyId +
				", creationDate=" + creationDate +
				", updatedAt=" + updatedAt +
				", deleted=" + deleted +
				", fromRevision=" + fromRevision +
				'}';
	}
}
