package com.db.persistence.scheme;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Date;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.EXISTING_PROPERTY, property="clz")
public abstract class BaseObject implements Serializable
{	
	private static final long serialVersionUID = 1L;

	public BaseObject() {
		this.keyId = new KeyId();
		this.clz = getClass();
	}

	public BaseObject(BaseObject baseObject) {
		keyId = new KeyId();
		this.clz = getClass();

		// New values for cloning
		this.deleted = baseObject.deleted;
		this.fromRevision = baseObject.fromRevision;
		this.entityManagerCtx = baseObject.entityManagerCtx;
	}

	/**
	 * Make a clone of the object (new UUID)
	 * @return
	 */
	@Transient
	public abstract BaseObject clone();// {return null;};

	/**
	 * Make a copy of the object, (same UUID)
	 * @return
	 */
	@Transient
    public BaseObject copy() {
	    BaseObject object = clone();
        object.setKeyId(this.getKeyId().copy());
	    return object;
    }

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
	@Transient
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
	@Transient
	@Temporal(TemporalType.TIMESTAMP)
	@Getter
	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	protected boolean deleted;

	@Getter
	public boolean isDeleted() {
		return deleted;
	}

	@XmlTransient
	@Value(value = "false")
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
	@Transient
	@Setter
	public void setFromRevision(int fromRevision) {
		this.fromRevision = fromRevision;
	}

	protected Class clz;

	@Getter
	public Class getClz() {return this.getClass();}

    @XmlTransient
    @Transient
    @Setter
	public void setClz(Class clz) {this.clz = clz;}

	public abstract void set(BaseObject baseObject);

	private Integer entityManagerCtx;

	public Integer getEntityManagerCtx() {
		return entityManagerCtx;
	}

	@XmlTransient
	@Transient
	@Setter
	public void setEntityManagerCtx(Integer entityManagerCtx) {
		this.entityManagerCtx = entityManagerCtx;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		BaseObject that = (BaseObject) o;

		if (deleted != that.deleted) return false;
		if (fromRevision != that.fromRevision) return false;
		if (keyId != null ? !keyId.equals(that.keyId) : that.keyId != null) return false;
		if (creationDate != null ? !creationDate.equals(that.creationDate) : that.creationDate != null) return false;
		if (updatedAt != null ? !updatedAt.equals(that.updatedAt) : that.updatedAt != null) return false;
		if (clz != null ? !clz.equals(that.clz) : that.clz != null) return false;
		return entityManagerCtx != null ? entityManagerCtx.equals(that.entityManagerCtx) : that.entityManagerCtx == null;
	}

	@Override
	public int hashCode() {
		int result = keyId != null ? keyId.hashCode() : 0;
		result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
		result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
		result = 31 * result + (deleted ? 1 : 0);
		result = 31 * result + fromRevision;
		result = 31 * result + (clz != null ? clz.hashCode() : 0);
		result = 31 * result + (entityManagerCtx != null ? entityManagerCtx.hashCode() : 0);
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
				", clz=" + clz +
				", entityManagerCtx=" + entityManagerCtx +
				'}';
	}
}
