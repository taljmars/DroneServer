package com.dronedb.server;

import javax.xml.ws.Endpoint;

import com.dronedb.persistence.ws.MissionFacadeRemote;
import org.springframework.stereotype.Component;

import com.dronedb.persistence.ws.DroneDbCrudSvcRemote;
import com.dronedb.persistence.ws.QuerySvcRemote;

@Component
public class DroneServer {
	

	public static void main(String[] args) {
		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory"); 
		
		DroneDbCrudSvcRemote droneDbCrudSvcRemote 	= AppConfig.context.getBean(DroneDbCrudSvcRemote.class);
		QuerySvcRemote querySvcRemote 				= AppConfig.context.getBean(QuerySvcRemote.class);
		MissionFacadeRemote missionFacadeRemote 	= AppConfig.context.getBean(MissionFacadeRemote.class);
		
		System.err.println("Sign " + DroneDbCrudSvcRemote.class.getSimpleName());
		Endpoint.publish("http://localhost:9999/ws/" + DroneDbCrudSvcRemote.class.getSimpleName(), droneDbCrudSvcRemote);
		
		System.err.println("Sign " + QuerySvcRemote.class.getSimpleName());
		Endpoint.publish("http://localhost:9999/ws/" + QuerySvcRemote.class.getSimpleName(), querySvcRemote);

		System.err.println("Sign " + MissionFacadeRemote.class.getSimpleName());
		Endpoint.publish("http://localhost:9999/ws/" + MissionFacadeRemote.class.getSimpleName(), missionFacadeRemote);
	}

}
