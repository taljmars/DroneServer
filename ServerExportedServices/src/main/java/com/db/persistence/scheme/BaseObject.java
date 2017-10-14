package com.db.persistence.scheme;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;

public abstract class BaseObject
{
	public BaseObject() {
	}

	private KeyId keyId;

	public KeyId getKeyId() {
		return keyId;
	}

	public void setKeyId(KeyId keyId) {
		this.keyId = keyId;
	}

	private Date creationDate;

	public void onCreate() {  
		this.creationDate = new Date();
	}

	@XmlTransient
	@Getter
	public Date getCreationDate() {  
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	private Date updatedAt;

	public void onUpdate() {
		this.updatedAt = new Date();
	}

	@XmlTransient
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
	@Setter
	public void setFromRevision(int fromRevision) {
		this.fromRevision = fromRevision;
	}

	protected Class clz;

	@Getter
	public Class getClz() {return this.getClass();}

    @XmlTransient
    @Setter
	public void setClz(Class clz) {this.clz = clz;}


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
        return clz != null ? clz.equals(that.clz) : that.clz == null;
    }

    @Override
    public int hashCode() {
        int result = keyId != null ? keyId.hashCode() : 0;
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + fromRevision;
        result = 31 * result + (clz != null ? clz.hashCode() : 0);
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
