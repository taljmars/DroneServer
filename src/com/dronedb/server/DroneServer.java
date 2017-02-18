package com.dronedb.server;

import javax.xml.ws.Endpoint;

import org.springframework.stereotype.Component;

import com.dronedb.ws.DroneDbCrudSvcRemote;
import com.dronedb.ws.QuerySvcRemote;

@Component
public class DroneServer {
	

	public static void main(String[] args) {
		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory"); 
		
		DroneDbCrudSvcRemote droneDbCrudSvcRemote = (DroneDbCrudSvcRemote) AppConfig.context.getBean("droneDbCrudSvcRemote");
		QuerySvcRemote querySvcRemote = (QuerySvcRemote) AppConfig.context.getBean("querySvcRemote");
		
		System.err.println("Sign " + DroneDbCrudSvcRemote.class.getSimpleName());
		Endpoint.publish("http://localhost:9999/ws/" + DroneDbCrudSvcRemote.class.getSimpleName(), droneDbCrudSvcRemote);
		
		System.err.println("Sign " + QuerySvcRemote.class.getSimpleName());
		Endpoint.publish("http://localhost:9999/ws/" + QuerySvcRemote.class.getSimpleName(), querySvcRemote);
	}

}
