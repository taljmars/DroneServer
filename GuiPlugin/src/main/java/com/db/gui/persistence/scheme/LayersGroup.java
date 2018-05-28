/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.gui.persistence.scheme;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.Sessionable;
import com.db.persistence.scheme.TargetType;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.db.persistence.scheme.Constants.GEN_CTX;

@NamedNativeQueries({
		@NamedNativeQuery(
				name = "GetAllLayersGroup",
				query = "SELECT * FROM LayersGroup WHERE " + GEN_CTX,
				resultClass = LayersGroup.class
		),
		@NamedNativeQuery(
				name = "GetLayersGroupById",
				query = "SELECT * FROM LayersGroup WHERE (objid = :OBJID) AND " + GEN_CTX,
				resultClass = LayersGroup.class
		),
		@NamedNativeQuery(
				name = "GetLayersGroupByName",
				query = "SELECT * FROM LayersGroup WHERE " + GEN_CTX + " AND (name ilike = :NAME)",
				resultClass = LayersGroup.class
		),
		@NamedNativeQuery(
				name = "GetAllModifiedLayersGroup",
				query = "SELECT * FROM LayersGroup WHERE " + GEN_CTX + " AND (entityManagerCtx != 0)",
				resultClass = LayersGroup.class
		)
})

@Table
@Entity
@Access(AccessType.FIELD)
@Sessionable
public class LayersGroup extends BaseLayer implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected boolean root;

	@ElementCollection(fetch = FetchType.EAGER)
	@TargetType(clz = Layer.class)
	@Fetch(value = FetchMode.SUBSELECT)
	protected List<String> layersUids;

	public LayersGroup() {
		super();
		layersUids = new ArrayList<>();
		root = false;
	}

	public LayersGroup(LayersGroup basicLayer) {
		super(basicLayer);
		this.root = basicLayer.root;
		this.layersUids = new ArrayList<>();
		for (String layerUid : basicLayer.getLayersUids()) {
			this.layersUids.add(layerUid);
		}
	}

	public LayersGroup(BaseLayer basicLayer) {
		super(basicLayer);
	}

	public LayersGroup clone() {
		return new LayersGroup(this);
	}

	public void set(BaseObject baseObject) {
		super.set(baseObject);
		LayersGroup basicLayer = (LayersGroup) baseObject;
		this.root = ((LayersGroup) baseObject).root;
		this.layersUids = new ArrayList<>();
		for (String layerUid : basicLayer.getLayersUids()) {
			this.layersUids.add(layerUid);
		}
	}

	@Override
	public BaseObject copy() {
		LayersGroup basicLayer = (LayersGroup) super.copy();
		return basicLayer;
	}

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }

	@Getter
	public List<String> getLayersUids() {
		return layersUids;
	}

	@Setter
	public void setLayersUids(List<String> layersUids) {
		this.layersUids.clear();
		if (layersUids != null)
			this.layersUids.addAll(layersUids);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		LayersGroup that = (LayersGroup) o;
		return root == that.root &&
				Objects.equals(layersUids, that.layersUids);
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), root, layersUids);
	}

	@Override
	public String toString() {
		return "LayersGroup{" +
				super.toString() +
				", root=" + root +
				", layersUids=" + layersUids +
				'}';
	}
}
