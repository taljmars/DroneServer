package com.dronedb.persistence.scheme;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-02-26T01:52:39.067+0200")
@StaticMetamodel(Circle.class)
public class Circle_ extends MissionItem_ {
	public static volatile SingularAttribute<Circle, Double> radius;
	public static volatile SingularAttribute<Circle, Integer> turns;
	public static volatile SingularAttribute<Circle, Double> lat;
	public static volatile SingularAttribute<Circle, Double> lng;
}
