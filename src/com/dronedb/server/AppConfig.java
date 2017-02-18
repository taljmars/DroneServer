package com.dronedb.server;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dronedb.services.DroneDbCrudSvc;
import com.dronedb.services.QuerySvc;
import com.dronedb.services.internal.DroneDbCrudSvcImpl;
import com.dronedb.services.internal.QuerySvcImpl;
import com.dronedb.ws.DroneDbCrudSvcRemote;
import com.dronedb.ws.QuerySvcRemote;
import com.dronedb.ws.internal.DroneDbCrudSvcRemoteImpl;
import com.dronedb.ws.internal.QuerySvcRemoteImpl;

@Configuration
public class AppConfig {
	
	public static ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
	
	private static EntityManagerFactory emFactory = null;
	private static EntityManager entityManager = null;
	
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
	
	@Bean
	public EntityManager entityManager() { 
		emFactory = Persistence.createEntityManagerFactory("DroneDB");
		entityManager = emFactory.createEntityManager( );
		return entityManager;
	}
	
	@PreDestroy
	private void closeDbConnection() {
		if (entityManager != null)
			entityManager.close();
		
		if (emFactory != null)
			emFactory.close();
	}

}
