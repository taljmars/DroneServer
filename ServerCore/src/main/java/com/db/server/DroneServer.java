package com.db.server;

import com.db.persistence.wsSoap.ObjectCrudSvcRemote;
import com.db.persistence.wsSoap.QuerySvcRemote;
import com.db.persistence.wsSoap.SessionsSvcRemote;
import com.plugins_manager.Plugins;
import org.apache.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.ws.Endpoint;

@Import({
		DroneDBServerAppConfig.class,
		DroneWeb.class,
		Plugins.class
})
@SpringBootApplication
public class DroneServer extends SpringBootServletInitializer
{
	private final static Logger LOGGER = Logger.getLogger(DroneServer.class);

	public static ApplicationContext context ;//= new AnnotationConfigApplicationContext(DroneDBServerAppConfig.class);

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DroneServer.class);
	}

	@Bean
	protected ServletContextListener listener() {
		return new ServletContextListener() {

			@Override
			public void contextInitialized(ServletContextEvent sce) {
				LOGGER.info("ServletContext initialized - TALMA");
			}

			@Override
			public void contextDestroyed(ServletContextEvent sce) {
				LOGGER.info("ServletContext destroyed - TALMA");
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

			context = ctx;

			LOGGER.debug("Details: " + logPath + " " + confPath);
			LOGGER.debug("Server is up and running!");
			System.err.println("Server is up and running!");
		};
	}

	private void go() {
//		System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");

		ObjectCrudSvcRemote objectCrudSvcRemote 		= context.getBean(ObjectCrudSvcRemote.class);
		QuerySvcRemote querySvcRemote 				= context.getBean(QuerySvcRemote.class);
//		MissionCrudSvcRemote missionCrudSvcRemote 		= DroneDBServerAppConfig.context.getBean(MissionCrudSvcRemote.class);
		SessionsSvcRemote sessionsSvcRemote 			= context.getBean(SessionsSvcRemote.class);
//		PerimeterCrudSvcRemote perimeterCrudSvcRemote 	= DroneDBServerAppConfig.context.getBean(PerimeterCrudSvcRemote.class);

		String ip = context.getBean("serverIp", String.class);
		String port = context.getBean("serverPort", String.class);

		String format = "http://%s:%s/wsSoap/%s";
		String url;

		url = String.format(format, ip, port, ObjectCrudSvcRemote.class.getSimpleName());
		LOGGER.debug("Sign " + ObjectCrudSvcRemote.class.getSimpleName() + " " + url);
		Endpoint.publish(url, objectCrudSvcRemote);

//		url = String.format(format, ip, port, QuerySvcRemote.class.getSimpleName());
//		LOGGER.debug("Sign " + QuerySvcRemote.class.getSimpleName() + " " + url);
////		endpoint = Endpoint.create(querySvcRemote);
////		endpoint.publish(String.format(format, ip, port, QuerySvcRemote.class.getSimpleName()));
//		Endpoint.publish(url, querySvcRemote);
//
//		url = String.format(format, ip, port, MissionCrudSvcRemote.class.getSimpleName());
//		LOGGER.debug("Sign " + MissionCrudSvcRemote.class.getSimpleName() + " " + url);
//		Endpoint.publish(url, missionCrudSvcRemote);
//
//		url = String.format(format, ip, port, SessionsSvcRemote.class.getSimpleName());
//		LOGGER.debug("Sign " + SessionsSvcRemote.class.getSimpleName() + " " + url);
//		Endpoint.publish(url, sessionsSvcRemote);
//
//		url = String.format(format, ip, port, PerimeterCrudSvcRemote.class.getSimpleName());
//		LOGGER.debug("Sign " + PerimeterCrudSvcRemote.class.getSimpleName() + " " + url);
//		Endpoint.publish(url, perimeterCrudSvcRemote);

		LOGGER.debug("Up and running!");
	}
}