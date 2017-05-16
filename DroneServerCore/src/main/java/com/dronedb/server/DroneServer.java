package com.dronedb.server;

import com.dronedb.persistence.scheme.*;
import org.springframework.stereotype.Component;

import javax.xml.ws.Endpoint;
import org.apache.log4j.Logger;

@Component
public class DroneServer {

	final static Logger logger = Logger.getLogger(DroneServer.class);

	public static void main(String[] args) {
		DroneServer droneServer	= DroneDBServerAppConfig.context.getBean(DroneServer.class);
		droneServer.go();
	}

	private void go() {
		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");

		DroneDbCrudSvcRemote droneDbCrudSvcRemote 		= DroneDBServerAppConfig.context.getBean(DroneDbCrudSvcRemote.class);
		QuerySvcRemote querySvcRemote 					= DroneDBServerAppConfig.context.getBean(QuerySvcRemote.class);
		MissionCrudSvcRemote missionCrudSvcRemote 		= DroneDBServerAppConfig.context.getBean(MissionCrudSvcRemote.class);
		SessionsSvcRemote sessionsSvcRemote 			= DroneDBServerAppConfig.context.getBean(SessionsSvcRemote.class);
		PerimeterCrudSvcRemote perimeterCrudSvcRemote 	= DroneDBServerAppConfig.context.getBean(PerimeterCrudSvcRemote.class);

		logger.debug("Sign " + DroneDbCrudSvcRemote.class.getSimpleName());
		Endpoint.publish("http://0.0.0.0:1234/ws/" + DroneDbCrudSvcRemote.class.getSimpleName(), droneDbCrudSvcRemote);

		logger.debug("Sign " + QuerySvcRemote.class.getSimpleName());
		Endpoint.publish("http://0.0.0.0:1234/ws/" + QuerySvcRemote.class.getSimpleName(), querySvcRemote);

		logger.debug("Sign " + MissionCrudSvcRemote.class.getSimpleName());
		Endpoint.publish("http://0.0.0.0:1234/ws/" + MissionCrudSvcRemote.class.getSimpleName(), missionCrudSvcRemote);

		logger.debug("Sign " + SessionsSvcRemote.class.getSimpleName());
		Endpoint.publish("http://0.0.0.0:1234/ws/" + SessionsSvcRemote.class.getSimpleName(), sessionsSvcRemote);

		logger.debug("Sign " + PerimeterCrudSvcRemote.class.getSimpleName());
		Endpoint.publish("http://0.0.0.0:1234/ws/" + PerimeterCrudSvcRemote.class.getSimpleName(), perimeterCrudSvcRemote);

		logger.debug("Up and running!");
	}
}