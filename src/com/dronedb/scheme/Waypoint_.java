package com.dronedb.scheme;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-02-18T18:20:09.203+0200")
@StaticMetamodel(Waypoint.class)
public class Waypoint_ extends MissionItem_ {
	public static volatile SingularAttribute<Waypoint, Double> delay;
	public static volatile SingularAttribute<Waypoint, Double> acceptanceRadius;
	public static volatile SingularAttribute<Waypoint, Double> yawAngle;
	public static volatile SingularAttribute<Waypoint, Double> orbitalRadius;
	public static volatile SingularAttribute<Waypoint, Boolean> orbitCCW;
	public static volatile SingularAttribute<Waypoint, Double> lat;
	public static volatile SingularAttribute<Waypoint, Double> lng;
}
