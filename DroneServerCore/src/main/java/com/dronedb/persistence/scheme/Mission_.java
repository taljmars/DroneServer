package com.dronedb.persistence.scheme;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-02-26T00:53:24.946+0200")
@StaticMetamodel(Mission.class)
public class Mission_ extends BaseObject_ {
	public static volatile SingularAttribute<Mission, String> name;
	public static volatile SingularAttribute<Mission, Double> defaultAlt;
	public static volatile ListAttribute<Mission, MissionItem> missionItems;
}
