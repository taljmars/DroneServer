package com.dronedb.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.ValidationMode;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
//@EnableTransactionManagement
@ComponentScan({ "com.dronedb.persistence" })
@ComponentScan({ "com.dronedb.server" })
@PropertySource({ "/com/dronedb/persistence-mysql.properties" })
public class PersistenceJPAConfig {

    private static final String FILENAME = "PASS_FILE";

    final static Logger logger = Logger.getLogger(DroneServer.class);

    @Autowired
    private Environment env;

    public PersistenceJPAConfig() {
        super();
        logger.debug("PersistenceJPAConfig Created");
    }

    private static String getPassword() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILENAME));
            return br.readLine();
        }
        catch (FileNotFoundException e) {
            logger.error("Password file doesn't exist", e);
        }
        catch (IOException e) {
            logger.error("Failed to read password file", e);
        }
        return null;
    }

    // beans

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[] { "com.dronedb.persistence.scheme" });

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        //TODO: Remove this limitation, we should enable auto-validate in the future
        // Prevent validation from automatic running during publish by hibernate
         em.setValidationMode(ValidationMode.NONE);

        return em;
    }

    @Bean
    public DataSource dataSource() {
    	DriverManagerDataSource dataSource = new DriverManagerDataSource();
    	dataSource.setDriverClassName("org.postgresql.Driver");
    	dataSource.setUrl("jdbc:postgresql://localhost:5432/dronedb");
    	dataSource.setUsername( "postgres" );
    	String pass = getPassword();
    	if (pass == null || pass.isEmpty())
    	    dataSource.setPassword( "postgres" );
    	else
    	    dataSource.setPassword(pass);
        return dataSource;
    }

//    @Bean
//    public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
//        final JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(emf);
//        return transactionManager;
//    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    final Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();
        // Flush the DB at the end
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
//        hibernateProperties.setProperty("hibernate.globally_quoted_identifiers", "true");
        return hibernateProperties;
    }

}