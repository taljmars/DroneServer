package com.db.server;

import com.db.persistence.wsSoap.ObjectCrudSvcRemote;
import com.db.persistence.wsSoap.QuerySvcRemote;
import com.db.persistence.wsSoap.SessionsSvcRemote;
import com.dronedb.persistence.ws.wsSoap.MissionCrudSvcRemote;
import com.dronedb.persistence.ws.wsSoap.PerimeterCrudSvcRemote;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.ws.Endpoint;
import org.apache.log4j.Logger;

@Import({DroneDBServerAppConfig.class, DroneWeb.class})
@SpringBootApplication
public class DroneServer extends SpringBootServletInitializer
{
	final static Logger logger = Logger.getLogger(DroneServer.class);

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DroneServer.class);
	}

	@Bean
	protected ServletContextListener listener() {
		return new ServletContextListener() {

			@Override
			public void contextInitialized(ServletContextEvent sce) {
				logger.info("ServletContext initialized - TALMA");
			}

			@Override
			public void contextDestroyed(ServletContextEvent sce) {
				logger.info("ServletContext destroyed - TALMA");
			}

		};
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(DroneServer.class, args);
	}

//	public void run(String[] args) {
	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			String logPath = "Logs";
			String confPath = "ServerCore/addon/conf/";
			if (args != null && args.length == 2) {
				logPath = args[0];
				confPath = args[1];
			}
			else {
				System.err.println("MISSING PARAMETERS");
			}

			// Debugs
			System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
			System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
			System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
			System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");

			// External Settings
			System.setProperty("LOGS.DIR", logPath);
			System.setProperty("CONF.DIR", confPath);

			DroneDBServerAppConfig.context = ctx;
	//		DroneServer droneServer = DroneDBServerAppConfig.context.getBean(DroneServer.class);
	//		droneServer.go();

			logger.debug("Details: " + logPath + " " + confPath);
			logger.debug("Server is up and running!");
			System.err.println("Server is up and running!");
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