package com.db.server;

import com.generic_tools.environment.Environment;
import com.generic_tools.validations.RuntimeValidator;
import org.apache.log4j.Logger;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Configuration
//@Import(PersistenceJPAConfigXml.class)
@Import({PersistenceJPAConfig.class, TransactionManagersConfig.class})
public class DroneDBServerAppConfig {

	private final static Logger LOGGER = Logger.getLogger(DroneDBServerAppConfig.class);

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
			LOGGER.error("IP file doesn't exist");
		}
		catch (IOException e) {
			LOGGER.error("Failed to read IP file");
		}
		//TODO: TALMA WA
		return "127.0.0.1";
		//return null;
	}

	@Bean
	public String serverPort() {
		try {
			String path = System.getProperty("CONF.DIR") + Environment.DIR_SEPERATOR + portFile;
			BufferedReader br = new BufferedReader(new FileReader(path));
			return br.readLine();
		}
		catch (FileNotFoundException e) {
			LOGGER.error("Port file doesn't exist");
		}
		catch (IOException e) {
			LOGGER.error("Failed to read port file");
		}
		//TODO: TALMA WA
		return "1234";
//		return null;
	}

	@Bean
	public Validator validator(final AutowireCapableBeanFactory autowireCapableBeanFactory) {
//		return new LocalValidatorFactoryBean();
		ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
				.configure().constraintValidatorFactory(new SpringConstraintValidatorFactory(autowireCapableBeanFactory))
				.buildValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		return validator;
	}

	@Bean
	public RuntimeValidator runtimeValidator(@Autowired Validator validator) {
		RuntimeValidator rtv = new RuntimeValidator();
		rtv.setValidator(validator);
		return rtv;
	}
}
