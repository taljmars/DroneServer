package com.dronedb.server;

import com.dronedb.persistence.scheme.*;
import org.springframework.stereotype.Component;

import javax.xml.ws.Endpoint;
import org.apache.log4j.Logger;

@Component
//@SpringBootApplication
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

		String ip = DroneDBServerAppConfig.context.getBean("serverIp", String.class);
		String port = DroneDBServerAppConfig.context.getBean("serverPort", String.class);

		String format = "http://%s:%s/ws/%s";
		String url;

		url = String.format(format, ip, port, DroneDbCrudSvcRemote.class.getSimpleName());
		logger.debug("Sign " + DroneDbCrudSvcRemote.class.getSimpleName() + " " + url);
//		Endpoint endpoint = Endpoint.create(droneDbCrudSvcRemote);
//		endpoint.publish(String.format(format, ip, port, DroneDbCrudSvcRemote.class.getSimpleName()));
		Endpoint.publish(url, droneDbCrudSvcRemote);

		url = String.format(format, ip, port, QuerySvcRemote.class.getSimpleName());
		logger.debug("Sign " + QuerySvcRemote.class.getSimpleName() + " " + url);
//		endpoint = Endpoint.create(querySvcRemote);
//		endpoint.publish(String.format(format, ip, port, QuerySvcRemote.class.getSimpleName()));
		Endpoint.publish(url, querySvcRemote);

		url = String.format(format, ip, port, MissionCrudSvcRemote.class.getSimpleName());
		logger.debug("Sign " + MissionCrudSvcRemote.class.getSimpleName() + " " + url);
		Endpoint.publish(url, missionCrudSvcRemote);

		url = String.format(format, ip, port, SessionsSvcRemote.class.getSimpleName());
		logger.debug("Sign " + SessionsSvcRemote.class.getSimpleName() + " " + url);
		Endpoint.publish(url, sessionsSvcRemote);

		url = String.format(format, ip, port, PerimeterCrudSvcRemote.class.getSimpleName());
		logger.debug("Sign " + PerimeterCrudSvcRemote.class.getSimpleName() + " " + url);
		Endpoint.publish(url, perimeterCrudSvcRemote);

		logger.debug("Up and running!");
	}
}