package com.dronedb.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.dronedb.persistence.services.DroneDbCrudSvc;
import com.dronedb.persistence.services.QuerySvc;
import com.dronedb.persistence.services.internal.DroneDbCrudSvcImpl;
import com.dronedb.persistence.services.internal.QuerySvcImpl;
import com.dronedb.persistence.ws.DroneDbCrudSvcRemote;
import com.dronedb.persistence.ws.QuerySvcRemote;
import com.dronedb.persistence.ws.internal.DroneDbCrudSvcRemoteImpl;
import com.dronedb.persistence.ws.internal.QuerySvcRemoteImpl;

@Configuration
@EnableTransactionManagement
@Import(PersistenceJPAConfigXml.class)
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
	
	@Bean
	public QuerySvcRemote querySvcRemote() {
		return new QuerySvcRemoteImpl();
	}
	
	@Bean
	public QuerySvc querySvc() {
		return new QuerySvcImpl();
	}
}
