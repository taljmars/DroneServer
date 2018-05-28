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
				name = "GetAllLayersGroupRoot",
				query = "SELECT * FROM LayersGroupRoot WHERE " + GEN_CTX,
				resultClass = LayersGroupRoot.class
		),
		@NamedNativeQuery(
				name = "GetLayersGroupRootById",
				query = "SELECT * FROM LayersGroupRoot WHERE (objid = :OBJID) AND " + GEN_CTX,
				resultClass = LayersGroupRoot.class
		),
		@NamedNativeQuery(
				name = "GetLayersGroupRootByName",
				query = "SELECT * FROM LayersGroupRoot WHERE " + GEN_CTX + " AND (name ilike = :NAME)",
				resultClass = LayersGroupRoot.class
		),
		@NamedNativeQuery(
				name = "GetAllModifiedLayersGroupRoot",
				query = "SELECT * FROM LayersGroupRoot WHERE " + GEN_CTX + " AND (entityManagerCtx != 0)",
				resultClass = LayersGroupRoot.class
		)
})

@Table
@Entity
@Access(AccessType.FIELD)
@Sessionable
public class LayersGroupRoot extends BaseLayer implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected boolean root;

	@ElementCollection(fetch = FetchType.EAGER)
	@TargetType(clz = LayersGroup.class)
	@Fetch(value = FetchMode.SUBSELECT)
	protected List<String> layersUids;

	public LayersGroupRoot() {
		super();
		layersUids = new ArrayList<>();
		root = false;
	}

	public LayersGroupRoot(LayersGroupRoot basicLayer) {
		super(basicLayer);
		this.root = basicLayer.root;
		this.layersUids = new ArrayList<>();
		for (String layerUid : basicLayer.getLayersUids()) {
			this.layersUids.add(layerUid);
		}
	}

	public LayersGroupRoot(BaseLayer basicLayer) {
		super(basicLayer);
	}

	public LayersGroupRoot clone() {
		return new LayersGroupRoot(this);
	}

	public void set(BaseObject baseObject) {
		super.set(baseObject);
		LayersGroupRoot basicLayer = (LayersGroupRoot) baseObject;
		this.root = ((LayersGroupRoot) baseObject).root;
		this.layersUids = new ArrayList<>();
		for (String layerUid : basicLayer.getLayersUids()) {
			this.layersUids.add(layerUid);
		}
	}

	@Override
	public BaseObject copy() {
		LayersGroupRoot basicLayer = (LayersGroupRoot) super.copy();
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
		LayersGroupRoot that = (LayersGroupRoot) o;
		return root == that.root &&
				Objects.equals(layersUids, that.layersUids);
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), root, layersUids);
	}

	@Override
	public String toString() {
		return "LayersGroupRoot{" +
				super.toString() +
				", root=" + root +
				", layersUids=" + layersUids +
				'}';
	}
}
