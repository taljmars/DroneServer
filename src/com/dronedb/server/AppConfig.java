package com.dronedb.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dronedb.services.DroneDbCrudSvc;
import com.dronedb.services.internal.DroneDbCrudSvcImpl;
import com.dronedb.ws.DroneDbCrudSvcRemote;
import com.dronedb.ws.internal.DroneDbCrudSvcRemoteImpl;

@Configuration
public class AppConfig {
	
	public static ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
	
	@Bean
	public DroneDbCrudSvcRemote droneDbCrudSvcRemote() {
		return new DroneDbCrudSvcRemoteImpl();
	}
	
	@Bean
	public DroneDbCrudSvc droneDbCrudSvc() {
		return new DroneDbCrudSvcImpl();
	}

}
