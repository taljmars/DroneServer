package com.dronedb.server;

import com.generic_tools.environment.Environment;
import com.generic_tools.validations.RuntimeValidator;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@EnableTransactionManagement
//@Import(PersistenceJPAConfigXml.class)
@Import(PersistenceJPAConfig.class)
public class DroneDBServerAppConfig {
	
	public static ApplicationContext context = new AnnotationConfigApplicationContext(DroneDBServerAppConfig.class);

	private static final Logger logger = Logger.getLogger(DroneDBServerAppConfig.class);

	private static final String portFile = "PORT";
	private static final String ipFile = "IP";

	@Bean
	public String serverIp() {
		try {
			String path = System.getProperty("CONF.DIR") + Environment.DIR_SEPERATOR + ipFile;
			FileReader fr = new FileReader(path);
			System.err.println(fr.toString());
			BufferedReader br = new BufferedReader(fr);
			return br.readLine();
		}
		catch (FileNotFoundException e) {
			logger.error("IP file doesn't exist", e);
		}
		catch (IOException e) {
			logger.error("Failed to read IP file", e);
		}
		return null;
	}

	@Bean
	public String serverPort() {
		try {
			String path = System.getProperty("CONF.DIR") + Environment.DIR_SEPERATOR + portFile;
			BufferedReader br = new BufferedReader(new FileReader(path));
			return br.readLine();
		}
		catch (FileNotFoundException e) {
			logger.error("Port file doesn't exist", e);
		}
		catch (IOException e) {
			logger.error("Failed to read port file", e);
		}
		return null;
	}

	@Bean
	public RuntimeValidator runtimeValidator() {
		return new RuntimeValidator();
	}
}
