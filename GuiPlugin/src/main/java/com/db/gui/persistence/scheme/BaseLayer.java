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

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.db.persistence.scheme.Constants.GEN_CTX;

//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
public abstract class BaseLayer extends BaseObject implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected String name;

	public BaseLayer() {
		super();
	}

	public BaseLayer(BaseLayer basicLayer) {
		super(basicLayer);
		this.name = basicLayer.getName();
	}

//	public BaseLayer clone() {
//		return new BaseLayer(this);
//	}

	public void set(BaseObject baseObject) {
		BaseLayer basicLayer = (BaseLayer) baseObject;
		this.name = basicLayer.getName();
	}

	@Override
	public BaseObject copy() {
		BaseLayer basicLayer = (BaseLayer) super.copy();
		basicLayer.setName(this.getName());
		return basicLayer;
	}

	@Getter
	public String getName() {
		return name;
	}

	@Setter
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		BaseLayer baseLayer = (BaseLayer) o;
		return Objects.equals(name, baseLayer.name);
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), name);
	}

	@Override
	public String toString() {
		return "BaseLayer{" +
				super.toString() +
				"name='" + name + '\'' +
				'}';
	}
}
