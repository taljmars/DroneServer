package com.dronedb.scheme;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table
public class MissionItem extends BaseObject implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString() {
		return "MissionItem [objid=" + objId + "]";
	}
}
