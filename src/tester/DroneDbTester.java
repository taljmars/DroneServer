package tester;

import com.dronedb.scheme.BaseObject;
import com.dronedb.scheme.Mission;
import com.dronedb.ws.DroneDbCrudSvcRemote;


public class DroneDbTester
{	
	public static void main(String[] args) {
		//DroneDbClient client = (DroneDbClient) AppConfig.context.getBean("droneDbClient");
		//System.err.println(client.getDroneDbCrudSvcRemote().CheckConnection());
		
		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory"); 
		
		DroneDbCrudSvcRemote droneDbCrudSvcRemote = (DroneDbCrudSvcRemote) AppConfig.context.getBean("droneDbCrudSvcRemote");
		System.err.println(droneDbCrudSvcRemote.CheckConnection());

//		Mission mission = droneDbCrudSvcRemote.create(Mission.class);
//		mission.setDefaultAlt(90);
//		droneDbCrudSvcRemote.update(mission);
		
		Mission object = droneDbCrudSvcRemote.readByClass(11, Mission.class);
		System.out.println(object.toString());
		object.setDefaultAlt(29);
		droneDbCrudSvcRemote.update(object);
	}

}
