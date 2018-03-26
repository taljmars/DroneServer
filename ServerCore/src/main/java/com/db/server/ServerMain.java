package com.db.server;

import com.plugins_manager.Plugins;
import org.apache.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import static com.db.server.SpringProfiles.H2;
import static com.db.server.SpringProfiles.Hibernate;

@Import({
		DatabaseServerConfig.class,
		WebConfig.class,
		Plugins.class,
		SecurityConfig.class
})
@ComponentScan({
		"com.events",
		"com.db.persistence.ws.internal",
		"com.db.persistence",
		"com.db.server",
		"com.db.aspects"
})
@PropertySource(value = "classpath:application.properties")
@SpringBootApplication
@EnableScheduling
public class ServerMain extends SpringBootServletInitializer
{
	private final static Logger LOGGER = Logger.getLogger(ServerMain.class);

	public static ApplicationContext context ;//= new AnnotationConfigApplicationContext(DatabaseServerConfig.class);

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        System.out.println("Start Server with profiles");
        loadProfile();
		return application.sources(ServerMain.class);
	}

	@Bean
	protected ServletContextListener listener() {
		return new ServletContextListener() {
			@Override public void contextInitialized(ServletContextEvent sce) { LOGGER.info("ServletContext initialized - TALMA"); }
			@Override public void contextDestroyed(ServletContextEvent sce) {
				LOGGER.info("ServletContext destroyed - TALMA");
			}
		};
	}

	public static void main(String[] args) throws Exception {
        System.out.println("Start Server with profiles");
        loadProfile();
		SpringApplication.run(ServerMain.class, args);
	}

	public static void loadProfile() {
        addSpringProfile(Hibernate);
//		addSpringProfile(EclipseLink);
//		addSpringProfile(Postgres);
        addSpringProfile(H2);
    }

	public static void addSpringProfile(String profile) {
		String prop = System.getProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME);
		if (prop == null)
			prop = profile;
		else
			prop += "," + profile;
		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, prop);
	}

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

			// External Settings
			System.setProperty("LOGS.DIR", logPath);
			System.setProperty("CONF.DIR", confPath);


			context = ctx;

			LOGGER.debug("Details: " + logPath + " " + confPath);
			LOGGER.debug("Server is up and running!\n\n");
			System.err.println("Server is up and running!");
		};
	}
}