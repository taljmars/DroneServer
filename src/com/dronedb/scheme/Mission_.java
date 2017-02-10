package com.dronedb.scheme;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-02-10T15:36:31.844+0200")
@StaticMetamodel(Mission.class)
public class Mission_ extends BaseObject_ {
	public static volatile SingularAttribute<Mission, Double> defaultAlt;
	public static volatile ListAttribute<Mission, MissionItem> items;
}
