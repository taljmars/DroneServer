package com.dronedb.tester;

//import com.dronedb.persistence.scheme.apis.*;
//import com.dronedb.persistence.scheme.mission.Mission;
//import com.dronedb.persistence.scheme.mission.MissionItem;
//import com.dronedb.persistence.scheme.mission.Takeoff;
//import com.dronedb.persistence.scheme.mission.Waypoint;
//import com.dronedb.persistence.scheme.perimeter.CirclePerimeter;
//import com.dronedb.persistence.scheme.perimeter.Point;

import com.dronedb.persistence.scheme.apis.*;
import com.dronedb.persistence.ws.internal.DroneDbCrudSvcRemote;


import javax.transaction.Transactional;

public class DroneDbTester
{	
	@Transactional
	public static void main(String[] args) {
		//DroneDbClient client = (DroneDbClient) AppConfig.context.getBean("droneDbClient");
		//System.err.println(client.getDroneDbCrudSvcRemote().CheckConnection());

		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory"); 
		
		DroneDbCrudSvcRemote droneDbCrudSvcRemote = (DroneDbCrudSvcRemote) AppConfig.context.getBean("droneDbCrudSvcRemote");
		//System.err.println(droneDbCrudSvcRemote.CheckConnection());

		System.out.println("Get mission");
		Mission mission = new Mission();
		mission.setDefaultAlt(2);
		mission.setName("Talma11");
		mission = (Mission) droneDbCrudSvcRemote.update(mission);
		
		System.out.println("Get waypoint");
		Waypoint waypoint = new Waypoint();
		waypoint.setOrbitalRadius(1);
		waypoint.setYawAngle(20);
		waypoint.setLat(11.1);
		waypoint.setLon(13.1);
		waypoint = (Waypoint) droneDbCrudSvcRemote.update(waypoint);
		mission.getMissionItemsUids().add(waypoint.getObjId());

		System.out.println("--> " + mission);
		mission = (Mission) droneDbCrudSvcRemote.update(mission);

		mission.getMissionItemsUids().remove(waypoint.getObjId());
		System.out.println("Get waypoint2");
		waypoint = new Waypoint();
		waypoint.setOrbitalRadius(6);
		waypoint.setYawAngle(60);
		waypoint.setLat(61.1);
		waypoint.setLon(63.1);
		waypoint = (Waypoint) droneDbCrudSvcRemote.update(waypoint);
		mission.getMissionItemsUids().add(waypoint.getObjId());

		mission.setName("Bob");
		mission = (Mission) droneDbCrudSvcRemote.update(mission);

		System.out.println("Mission created");

		CirclePerimeter circlePerimeter = new CirclePerimeter();
		circlePerimeter.setRadius(10.0);
		circlePerimeter.setName("tal");
		//circlePerimeter.setCenter(new Point(1.0, 1.0));
		//droneDbCrudSvcRemote.update(circlePerimeter);

		circlePerimeter.setName("tatatat");
		//circlePerimeter.getCenter().setLat(3.0);
		//droneDbCrudSvcRemote.update(circlePerimeter);


//		QuerySvcRemote querySvcRemote = (QuerySvcRemote) AppConfig.context.getBean("querySvcRemote");
//		QueryRequestRemote queryRequestRemote = new QueryRequestRemote();
//		queryRequestRemote.setQuery("GetMissionById");
//		queryRequestRemote.setClz(Mission.class);
//		queryRequestRemote.addParameter("OBJID", mission.getObjId());
//		QueryResponseRemote list = querySvcRemote.query(queryRequestRemote);
//		System.err.println(list.getResultList());
//		Mission m = (Mission) list.getResultList().get(0);
//		System.out.println(m.getMissionItemsUids().get(0).toString());
//
//		System.out.println("Sending delete");
//		droneDbCrudSvcRemote.delete(m);
	}

}
