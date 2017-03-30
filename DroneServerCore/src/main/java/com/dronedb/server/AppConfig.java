package com.dronedb.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
//@Import(PersistenceJPAConfigXml.class)
@Import(PersistenceJPAConfig.class)
public class AppConfig {
	
	public static ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

}
