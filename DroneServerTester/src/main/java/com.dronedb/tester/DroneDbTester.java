package com.dronedb.tester;

import com.dronedb.persistence.ws.DroneDbCrudSvcRemote;
import com.dronedb.persistence.ws.QueryRequestRemote;
import com.dronedb.persistence.ws.QueryResponseRemote;
import com.dronedb.persistence.ws.QuerySvcRemote;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.dronedb.persistence.scheme.*;

public class DroneDbTester
{	
	@Transactional
	public static void main(String[] args) {
		//DroneDbClient client = (DroneDbClient) AppConfig.context.getBean("droneDbClient");
		//System.err.println(client.getDroneDbCrudSvcRemote().CheckConnection());
		
		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory"); 
		
		DroneDbCrudSvcRemote droneDbCrudSvcRemote = (DroneDbCrudSvcRemote) AppConfig.context.getBean("droneDbCrudSvcRemote");
		System.err.println(droneDbCrudSvcRemote.CheckConnection());

		System.out.println("Get mission");
		Mission mission = droneDbCrudSvcRemote.create(Mission.class);
		mission.setDefaultAlt(2);
		//droneDbCrudSvcRemote.update(mission);
		
//		System.out.println("Get waypoint");
//		Waypoint waypoint = droneDbCrudSvcRemote.create(Waypoint.class);
//		if (waypoint == null) {
//			System.err.println("Failed to get waypoint object");
//			return;
//		}
//		else {
//			System.out.println(waypoint.getObjId());
//		}
//		
//		waypoint.setDelay(2);
//		waypoint.setOrbitalRadius(1);
//		waypoint.setYawAngle(20);
		//waypoint.missionOwner = mission;
		//waypoint.setMission(mission);
		
		
		//mission.addMissionItem(waypoint);
//		
//		ReturnToHome returnToHome = droneDbCrudSvcRemote.create(ReturnToHome.class);
//		returnToHome.setReturnAltitude(1);
		//returnToHome.missionOwner = mission;
		//returnToHome.setMission(mission);
		
//		System.out.println(returnToHome);
		
		//mission.addMissionItem(returnToHome);
		
//		System.out.println("Get update for " + waypoint.toString());
//		droneDbCrudSvcRemote.update(waypoint);
//		droneDbCrudSvcRemote.update(returnToHome);
		//mission = droneDbCrudSvcRemote.update(mission);
		System.out.println("Mission created");
		Waypoint missionItemw = droneDbCrudSvcRemote.create(Waypoint.class);
		Takeoff missionItemt = droneDbCrudSvcRemote.create(Takeoff.class);
		//missionItem.setMission(mission);
		missionItemw = droneDbCrudSvcRemote.update(missionItemw);
		missionItemt = droneDbCrudSvcRemote.update(missionItemt);
		System.out.println("MissionItem created");		
		//mission.setMissionItems3(Arrays.asList(waypoint, returnToHome));
		List<MissionItem> set = new ArrayList();
		set.add(missionItemw);
		set.add(missionItemt);
		mission.setMissionItems(set);
		mission = droneDbCrudSvcRemote.update(mission);
		//missionItem.setMission(mission);
		
		System.out.println("Read mission");
		Mission object = droneDbCrudSvcRemote.readByClass(mission.getObjId(), Mission.class);
		System.out.println(object.toString());
//		object.setDefaultAlt(29);
//		droneDbCrudSvcRemote.update(object);
		
		System.out.println("Read waypoint");
//		waypoint = droneDbCrudSvcRemote.readByClass(waypoint.getObjId(), Waypoint.class);
//		System.out.println(waypoint.toString());
		//System.out.println(waypoint.getMission());
//		
		QuerySvcRemote querySvcRemote = (QuerySvcRemote) AppConfig.context.getBean("querySvcRemote");
//		QueryResponseRemote list = querySvcRemote.runNativeQuery("SELECT * FROM missionitems c WHERE c.type='com.dronedb.scheme.MissionItem.MissionItemType.Waypoint'", Waypoint.class);
//		QueryResponseRemote list = querySvcRemote.runNativeQuery("SELECT * FROM missionitems WHERE missionOwner_objid='" + mission.getObjId() + "'", MissionItem.class);
//		System.err.println(list.getResultList());
		
		QueryRequestRemote queryRequestRemote = new QueryRequestRemote();
		queryRequestRemote.setQuery("GetMissionById");
		queryRequestRemote.setClz(Mission.class);
		queryRequestRemote.addParameter("OBJID", mission.getObjId());
		QueryResponseRemote list = querySvcRemote.query(queryRequestRemote);
		System.err.println(list.getResultList());
		Mission m = (Mission) list.getResultList().get(0);
		System.out.println(m.getMissionItems().get(0).toString());
		
		System.out.println("Sending delete");
		droneDbCrudSvcRemote.delete(m);
	}

}
