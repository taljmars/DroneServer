package com.db.server;

import com.db.server.entityManager.MyEntityManagerFactory;
import com.db.server.transactions.TransactionManagersConfig;
import com.generic_tools.environment.Environment;
import com.generic_tools.validations.RuntimeValidator;
import org.apache.log4j.Logger;
import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Configuration
@Import({MyEntityManagerFactory.class, TransactionManagersConfig.class})
public class DatabaseServerConfig {

	private final static Logger LOGGER = Logger.getLogger(DatabaseServerConfig.class);

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
