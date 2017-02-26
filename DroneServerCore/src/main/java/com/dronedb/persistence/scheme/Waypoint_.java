package com.dronedb.persistence.scheme;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-02-26T01:52:39.080+0200")
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
