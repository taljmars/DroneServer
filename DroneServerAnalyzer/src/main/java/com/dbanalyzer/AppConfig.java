package com.dbanalyzer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@ComponentScan("com.dbanalyzer")
@ComponentScan("com.db.persistence.exceptionAdviser")
@Configuration
public class AppConfig
{
	public static ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);


}
