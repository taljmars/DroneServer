package com.db.server;

import com.db.persistence.wsSoap.ObjectCrudSvcRemote;
import com.db.persistence.wsSoap.QuerySvcRemote;
import com.db.persistence.wsSoap.SessionsSvcRemote;
import com.dronedb.persistence.ws.wsSoap.MissionCrudSvcRemote;
import com.dronedb.persistence.ws.wsSoap.PerimeterCrudSvcRemote;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.xml.ws.Endpoint;
import org.apache.log4j.Logger;

//@Component
@Import(DroneDBServerAppConfig.class)
//@ComponentScan({ "com.db.persistence.wsRest.internal" })
@SpringBootApplication
public class DroneServer extends SpringBootServletInitializer {

	final static Logger logger = Logger.getLogger(DroneServer.class);

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DroneServer.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(DroneServer.class, args);
	}

//	public void run(String[] args) {
@Bean
public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
	return args -> {
		System.err.println("TALMA TALMA" + args[0] + " " + args[1]);
		// Debugs
		System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
		System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
		System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");


		System.setProperty("LOGS.DIR", args[0]);
		System.setProperty("CONF.DIR", args[1]);

		DroneDBServerAppConfig.context = ctx;
//		DroneServer droneServer = DroneDBServerAppConfig.context.getBean(DroneServer.class);
//		droneServer.go();
	};
}

	private void go() {
//		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");

		ObjectCrudSvcRemote objectCrudSvcRemote 		= DroneDBServerAppConfig.context.getBean(ObjectCrudSvcRemote.class);
		QuerySvcRemote querySvcRemote 				= DroneDBServerAppConfig.context.getBean(QuerySvcRemote.class);
		MissionCrudSvcRemote missionCrudSvcRemote 		= DroneDBServerAppConfig.context.getBean(MissionCrudSvcRemote.class);
		SessionsSvcRemote sessionsSvcRemote 			= DroneDBServerAppConfig.context.getBean(SessionsSvcRemote.class);
		PerimeterCrudSvcRemote perimeterCrudSvcRemote 	= DroneDBServerAppConfig.context.getBean(PerimeterCrudSvcRemote.class);

		String ip = DroneDBServerAppConfig.context.getBean("serverIp", String.class);
		String port = DroneDBServerAppConfig.context.getBean("serverPort", String.class);

		String format = "http://%s:%s/wsSoap/%s";
		String url;

		url = String.format(format, ip, port, ObjectCrudSvcRemote.class.getSimpleName());
		logger.debug("Sign " + ObjectCrudSvcRemote.class.getSimpleName() + " " + url);
		Endpoint.publish(url, objectCrudSvcRemote);

//		url = String.format(format, ip, port, QuerySvcRemote.class.getSimpleName());
//		logger.debug("Sign " + QuerySvcRemote.class.getSimpleName() + " " + url);
////		endpoint = Endpoint.create(querySvcRemote);
////		endpoint.publish(String.format(format, ip, port, QuerySvcRemote.class.getSimpleName()));
//		Endpoint.publish(url, querySvcRemote);
//
//		url = String.format(format, ip, port, MissionCrudSvcRemote.class.getSimpleName());
//		logger.debug("Sign " + MissionCrudSvcRemote.class.getSimpleName() + " " + url);
//		Endpoint.publish(url, missionCrudSvcRemote);
//
//		url = String.format(format, ip, port, SessionsSvcRemote.class.getSimpleName());
//		logger.debug("Sign " + SessionsSvcRemote.class.getSimpleName() + " " + url);
//		Endpoint.publish(url, sessionsSvcRemote);
//
//		url = String.format(format, ip, port, PerimeterCrudSvcRemote.class.getSimpleName());
//		logger.debug("Sign " + PerimeterCrudSvcRemote.class.getSimpleName() + " " + url);
//		Endpoint.publish(url, perimeterCrudSvcRemote);

		logger.debug("Up and running!");
	}
}