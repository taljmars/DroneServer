package com.dronedb.server;

import com.generic_tools.validations.RuntimeValidator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
//@Import(PersistenceJPAConfigXml.class)
@Import(PersistenceJPAConfig.class)
public class DroneDBServerAppConfig {
	
	public static ApplicationContext context = new AnnotationConfigApplicationContext(DroneDBServerAppConfig.class);

	@Bean
	public RuntimeValidator runtimeValidator() {
		return new RuntimeValidator();
	}

}
