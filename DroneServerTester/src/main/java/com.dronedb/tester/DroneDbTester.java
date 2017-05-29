package com.dronedb.tester;

import com.dronedb.persistence.scheme.*;
import com.dronedb.persistence.ws.internal.ObjectNotFoundException;
import com.dronedb.persistence.ws.internal.DroneDbCrudSvcRemote;
import com.dronedb.persistence.ws.internal.QuerySvcRemote;
import com.dronedb.persistence.ws.internal.SessionsSvcRemote;
import javax.transaction.Transactional;

public class DroneDbTester
{	
	@Transactional
	public static void main(String[] args) throws com.dronedb.persistence.ws.internal.DatabaseValidationRemoteException, ObjectNotFoundException {
		//DroneDbClient client = (DroneDbClient) AppConfig.context.getBean("droneDbClient");
		//System.err.println(client.getDroneDbCrudSvcRemote().CheckConnection());

		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory"); 
		
		DroneDbCrudSvcRemote droneDbCrudSvcRemote = AppConfig.context.getBean(DroneDbCrudSvcRemote.class);
		SessionsSvcRemote sessionsSvcRemote = AppConfig.context.getBean(SessionsSvcRemote.class);
		//System.err.println(droneDbCrudSvcRemote.CheckConnection());

		System.out.println("Get mission");
		Mission mission = (Mission) droneDbCrudSvcRemote.create(Mission.class.getName());
		mission.setDefaultAlt(2);
		mission.setName("TalVer3");
		mission = (Mission) droneDbCrudSvcRemote.update(mission);
		
		System.out.println("Get waypoint");
		Waypoint waypoint = (Waypoint) droneDbCrudSvcRemote.create(Waypoint.class.getName());
		waypoint.setOrbitalRadius(1);
		waypoint.setYawAngle(20);
		waypoint.setLat(11.1);
		waypoint.setLon(13.1);
		waypoint = (Waypoint) droneDbCrudSvcRemote.update(waypoint);
		mission.getMissionItemsUids().add(waypoint.getKeyId().getObjId());

		System.out.println("--> " + mission);
		mission = (Mission) droneDbCrudSvcRemote.update(mission);

		mission.getMissionItemsUids().remove(waypoint.getKeyId().getObjId());
		System.out.println("Get waypoint2");
		waypoint = (Waypoint) droneDbCrudSvcRemote.create(Waypoint.class.getName());
		waypoint.setOrbitalRadius(6);
		waypoint.setYawAngle(60);
		waypoint.setLat(61.1);
		waypoint.setLon(63.1);

		waypoint = (Waypoint) droneDbCrudSvcRemote.update(waypoint);
		mission.getMissionItemsUids().add(waypoint.getKeyId().getObjId());

		System.out.println("--> " + mission);
		mission = (Mission) droneDbCrudSvcRemote.update(mission);

		System.out.println("Mission created");

//		Mission m = (Mission) list.getResultList().get(0);
//		System.out.println(m.getMissionItemsUids().get(0).toString());
//
//		System.out.println("Sending delete");
//		droneDbCrudSvcRemote.delete(m);

		sessionsSvcRemote.publish();

		mission.setName("TalVer2");
		droneDbCrudSvcRemote.update(mission);

		sessionsSvcRemote.publish();

		mission.setName("Should not seen");
		droneDbCrudSvcRemote.update(mission);
		sessionsSvcRemote.discard();


		droneDbCrudSvcRemote.delete(mission);
		sessionsSvcRemote.publish();

		mission = (Mission) droneDbCrudSvcRemote.create(Mission.class.getName());
		mission.setName("in private");
		droneDbCrudSvcRemote.update(mission);

		QuerySvcRemote querySvcRemote = AppConfig.context.getBean(QuerySvcRemote.class);
		QueryRequestRemote queryRequestRemote = new QueryRequestRemote();
		queryRequestRemote.setQuery("GetAllMissions");
		queryRequestRemote.setClz(Mission.class.toString());
		QueryResponseRemote list = querySvcRemote.query(queryRequestRemote);
		System.err.println(list.getResultList());

		BaseObject object = droneDbCrudSvcRemote.read(mission.getKeyId().getObjId().toString());
		System.err.println(object.toString());

		sessionsSvcRemote.publish();
	}

}
