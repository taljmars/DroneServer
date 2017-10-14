package com.dronedb.tester;

import com.db.persistence.scheme.DummyBaseObject;
import com.db.persistence.wsRest.ObjectCrudSvcRemote;

import javax.transaction.Transactional;

public class DroneDbTester
{	
	@Transactional
	public static void main(String[] args) throws Exception {
		//DroneDbClient client = (DroneDbClient) AppConfig.context.getBean("droneDbClient");
		//System.err.println(client.getobjectCrudSvcRemote().CheckConnection());

//		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");
		
		ObjectCrudSvcRemote objectCrudSvcRemote = AppConfig.context.getBean(ObjectCrudSvcRemote.class);
//		MissionCrudSvcRemote missionCrudSvcRemote = AppConfig.context.getBean(MissionCrudSvcRemote.class);
//		PerimeterCrudSvcRemote perimeterCrudSvcRemote = AppConfig.context.getBean(PerimeterCrudSvcRemote.class);
//		SessionsSvcRemote sessionsSvcRemote = AppConfig.context.getBean(SessionsSvcRemote.class);
		//System.err.println(objectCrudSvcRemote.CheckConnection());

		DummyBaseObject d = (DummyBaseObject) objectCrudSvcRemote.create(DummyBaseObject.class.getCanonicalName());

//		missionCrudSvcRemote.createMissionItem(Waypoint.class.getCanonicalName());
//		objectCrudSvcRemote.create(Mission.class.toString());
//		perimeterCrudSvcRemote.createPoint();

//		System.out.println("Get perimeter");
//		CirclePerimeter circlePerimeter = (CirclePerimeter) objectCrudSvcRemote.create(CirclePerimeter.class.getName());
//		System.out.println("perimeter: " + circlePerimeter);
//
//		PolygonPerimeter polygonPerimeter = (PolygonPerimeter) objectCrudSvcRemote.create(PolygonPerimeter.class.getName());
//		System.out.println("perimeter: " + polygonPerimeter);
//
//		System.out.println("Get mission");
//		Mission mission = (Mission) objectCrudSvcRemote.create(Mission.class.getName());
//		mission.setDefaultAlt(2);
//		mission.setName("TalVer3");
//		mission = (Mission) objectCrudSvcRemote.update(mission);
//
//		System.out.println("Get waypoint");
//		Waypoint waypoint = (Waypoint) objectCrudSvcRemote.create(Waypoint.class.getName());
//		waypoint.setOrbitalRadius(1);
//		waypoint.setYawAngle(20);
//		waypoint.setLat(11.1);
//		waypoint.setLon(13.1);
//		waypoint = (Waypoint) objectCrudSvcRemote.update(waypoint);
//		mission.getMissionItemsUids().add(waypoint.getKeyId().getObjId());
//
//		System.out.println("--> " + mission);
//		mission = (Mission) objectCrudSvcRemote.update(mission);
//
//		mission.getMissionItemsUids().remove(waypoint.getKeyId().getObjId());
//		System.out.println("Get waypoint2");
//		waypoint = (Waypoint) objectCrudSvcRemote.create(Waypoint.class.getName());
//		waypoint.setOrbitalRadius(6);
//		waypoint.setYawAngle(60);
//		waypoint.setLat(61.1);
//		waypoint.setLon(63.1);
//
//		waypoint = (Waypoint) objectCrudSvcRemote.update(waypoint);
//		mission.getMissionItemsUids().add(waypoint.getKeyId().getObjId());
//
//		System.out.println("--> " + mission);
//		mission = (Mission) objectCrudSvcRemote.update(mission);
//
//		System.out.println("Mission created");
//
////		Mission m = (Mission) list.getResultList().get(0);
////		System.out.println(m.getMissionItemsUids().get(0).toString());
////
////		System.out.println("Sending delete");
////		objectCrudSvcRemote.delete(m);
//
//		sessionsSvcRemote.publish();
//
//		mission.setName("TalVer2");
//		objectCrudSvcRemote.update(mission);
//
//		sessionsSvcRemote.publish();
//
//		mission.setName("Should not seen");
//		objectCrudSvcRemote.update(mission);
//		sessionsSvcRemote.discard();
//
//
//		objectCrudSvcRemote.delete(mission);
//		sessionsSvcRemote.publish();
//
//		mission = (Mission) objectCrudSvcRemote.create(Mission.class.toString());
//		mission.setName("in private");
//		objectCrudSvcRemote.update(mission);
//
////		QuerySvcRemote querySvcRemote = AppConfig.context.getBean(QuerySvcRemote.class);
////		QueryRequestRemote queryRequestRemote = new QueryRequestRemote();
////		queryRequestRemote.setQuery("GetAllMissions");
////		queryRequestRemote.setClz(Mission.class.toString());
////		QueryResponseRemote list = querySvcRemote.query(queryRequestRemote);
////		System.err.println(list.getResultList());
////
////		BaseObject object = objectCrudSvcRemote.read(mission.getKeyId().getObjId().toString());
////		System.err.println(object.toString());
//
//		sessionsSvcRemote.publish();
	}

}
