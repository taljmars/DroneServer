package com.dronedb.scheme;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-02-18T18:20:09.115+0200")
@StaticMetamodel(Circle.class)
public class Circle_ extends MissionItem_ {
	public static volatile SingularAttribute<Circle, Double> lat;
	public static volatile SingularAttribute<Circle, Double> lng;
	public static volatile SingularAttribute<Circle, Double> radius;
	public static volatile SingularAttribute<Circle, Integer> turns;
}
