/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.gui.persistence.scheme;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.Sessionable;
import com.db.persistence.scheme.TargetExcludeTypes;
import com.db.persistence.scheme.TargetType;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.db.persistence.scheme.Constants.GEN_CTX;

@NamedNativeQueries({
		@NamedNativeQuery(
				name = "GetAllLayers",
				query = "SELECT * FROM Layer WHERE " + GEN_CTX,
				resultClass = Layer.class
		),
		@NamedNativeQuery(
				name = "GetLayerById",
				query = "SELECT * FROM Layer WHERE (objid = :OBJID) AND " + GEN_CTX,
				resultClass = Layer.class
		),
		@NamedNativeQuery(
				name = "GetLayerByName",
				query = "SELECT * FROM Layer WHERE " + GEN_CTX + " AND (name ilike = :NAME)",
				resultClass = Layer.class
		),
		@NamedNativeQuery(
				name = "GetAllModifiedLayers",
				query = "SELECT * FROM Layer WHERE " + GEN_CTX + " AND (entityManagerCtx != 0)",
				resultClass = Layer.class
		)
})

@Table
@Entity
@Access(javax.persistence.AccessType.FIELD)
@Sessionable
public class Layer extends BaseLayer implements Serializable
{
	private static final long serialVersionUID = 1L;

	@ElementCollection(fetch = FetchType.EAGER)
	@TargetType(clz = BaseObject.class)
	@TargetExcludeTypes(classes = {LayersGroup.class, Layer.class, BaseLayer.class})
	protected List<String> objectsUids;

	public Layer() {
		super();
		objectsUids = new ArrayList<>();
	}

	public Layer(Layer basicLayer) {
		super(basicLayer);
		this.objectsUids = new ArrayList<>();
		for (String objectUid : basicLayer.getObjectsUids()) {
			this.objectsUids.add(objectUid);
		}
	}

	public Layer clone() {
		return new Layer(this);
	}

	public void set(BaseObject baseObject) {
		super.set(baseObject);
		Layer basicLayer = (Layer) baseObject;
		this.objectsUids = new ArrayList<>();
		for (String objectUid : basicLayer.getObjectsUids()) {
			this.objectsUids.add(objectUid);
		}
	}

	@Override
	public BaseObject copy() {
		Layer basicLayer = (Layer) super.copy();
		return basicLayer;
	}

	@Getter
	public List<String> getObjectsUids() {
		return objectsUids;
	}

	@Setter
	public void setObjectsUids(List<String> objectsUids) {
		this.objectsUids.clear();
		if (objectsUids != null)
			this.objectsUids.addAll(objectsUids);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		Layer layer = (Layer) o;
		return Objects.equals(objectsUids, layer.objectsUids);
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), objectsUids);
	}

	@Override
	public String toString() {
		return "Layer{" +
				super.toString() +
				", objectsUids=" + objectsUids +
				'}';
	}
}
