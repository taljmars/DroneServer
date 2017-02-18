package tester;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import com.dronedb.scheme.BaseObject;
import com.dronedb.scheme.Circle;
import com.dronedb.scheme.Mission;
import com.dronedb.scheme.MissionItem;
import com.dronedb.scheme.ReturnToHome;
import com.dronedb.scheme.Waypoint;
import com.dronedb.ws.DroneDbCrudSvcRemote;
import com.dronedb.ws.QueryResponseRemote;
import com.dronedb.ws.QuerySvcRemote;


public class DroneDbTester
{	
	public static void main(String[] args) {
		//DroneDbClient client = (DroneDbClient) AppConfig.context.getBean("droneDbClient");
		//System.err.println(client.getDroneDbCrudSvcRemote().CheckConnection());
		
		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory"); 
		
		DroneDbCrudSvcRemote droneDbCrudSvcRemote = (DroneDbCrudSvcRemote) AppConfig.context.getBean("droneDbCrudSvcRemote");
		System.err.println(droneDbCrudSvcRemote.CheckConnection());

		System.out.println("Get mission");
		Mission mission = droneDbCrudSvcRemote.create(Mission.class);
		
		System.out.println("Get waypoint");
		Waypoint waypoint = droneDbCrudSvcRemote.create(Waypoint.class);
		if (waypoint == null) {
			System.err.println("Failed to get waypoint object");
			return;
		}
		else {
			System.out.println(waypoint.getObjId());
		}
		
		waypoint.setDelay(2);
		waypoint.setOrbitalRadius(1);
		waypoint.setYawAngle(20);
		
		mission.addMissionItem(waypoint);
		
		ReturnToHome returnToHome = droneDbCrudSvcRemote.create(ReturnToHome.class);
		returnToHome.setReturnAltitude(1);
		System.out.println(returnToHome);
		
		mission.addMissionItem(returnToHome);
		
		System.out.println("Get update for " + waypoint.toString());
		droneDbCrudSvcRemote.update(waypoint);
		droneDbCrudSvcRemote.update(returnToHome);
		droneDbCrudSvcRemote.update(mission);
		
		
		Mission object = droneDbCrudSvcRemote.readByClass(1, Mission.class);
		System.out.println(object.toString());
//		object.setDefaultAlt(29);
//		droneDbCrudSvcRemote.update(object);
//		
//		QuerySvcRemote querySvcRemote = (QuerySvcRemote) AppConfig.context.getBean("querySvcRemote");
//		QueryResponseRemote list = querySvcRemote.runNativeQuery("SELECT * FROM missionitems c WHERE c.type='com.dronedb.scheme.MissionItem.MissionItemType.Waypoint'", Waypoint.class);
//		QueryResponseRemote list = querySvcRemote.runNativeQuery("SELECT * FROM missionitems", MissionItem.class);
//		System.err.println(list.getResultList());
//		list = querySvcRemote.runNamedQuery("getAllWaypoints", MissionItem.class);
//		System.err.println(list.getResultList());
	}

}
