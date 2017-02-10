package com.dronedb.server;

import javax.xml.ws.Endpoint;

import org.springframework.stereotype.Component;

import com.dronedb.ws.DroneDbCrudSvcRemote;

@Component
public class DroneServer {
	

	public static void main(String[] args) {
		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory"); 
		
		DroneDbCrudSvcRemote droneDbCrudSvcRemote = (DroneDbCrudSvcRemote) AppConfig.context.getBean("droneDbCrudSvcRemote");
		
		Endpoint.publish("http://localhost:9999/ws/droneServer", droneDbCrudSvcRemote);
	}

}
