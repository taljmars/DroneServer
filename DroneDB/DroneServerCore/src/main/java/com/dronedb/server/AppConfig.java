package com.dronedb.server;

import com.dronedb.persistence.services.MissionFacadeSvc;
import com.dronedb.persistence.services.internal.MissionFacadeSvcImpl;
import com.dronedb.persistence.ws.MissionFacadeRemote;
import com.dronedb.persistence.ws.internal.MissionFacadeRemoteImpl;
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

}
