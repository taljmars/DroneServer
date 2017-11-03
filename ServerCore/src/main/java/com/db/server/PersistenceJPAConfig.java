package com.db.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.ValidationMode;
import javax.sql.DataSource;

import com.plugins_manager.PluginsManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
//@EnableTransactionManagement
@ComponentScan({ "com.dronedb.persistence", "com.db.persistence" })
@ComponentScan({ "com.db.persistence" })
@ComponentScan({ "com.db.persistence.wsRest.internal" })
@ComponentScan({ "com.db.server" })
@ComponentScan({ "com.db.persistence.wsRest.internal" })
//@EnableWebMvc
//@PropertySource({ "/com/dronedb/persistence-mysql.properties" })
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
            logger.error("Password file doesn't exist");
        }
        catch (IOException e) {
            logger.error("Failed to read password file");
        }
        //TODO: TALMA WA
        return "postgres";
//        return null;
    }

    // beans

    @Bean
    @Lazy
    public EntityManager createEntityManager(@Autowired EntityManagerFactory entityManagerFactory) {
        System.out.println("TALMA WIN!!!!!");
        return SharedEntityManagerCreator.createSharedEntityManager(entityManagerFactory);
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
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Autowired DataSource dataSource) {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(dataSource);
        PluginsManager pluginsManager = PluginsManager.getInstance();
        List<String> schemesLocation = pluginsManager.getSchemes();

        String[] schemes = new String[schemesLocation.size()];
        logger.debug("Loading Scheme to DB");
//        em.setPackagesToScan(new String[] { "com.db.persistence.scheme", "com.dronedb.persistence.scheme" });
        schemes = schemesLocation.toArray(schemes);
        em.setPackagesToScan(schemes);

        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        //TODO: Remove this limitation, we should enable auto-validate in the future
        // Prevent validation from automatic running during publish by hibernate
        em.setValidationMode(ValidationMode.NONE);

        return em;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    final Properties additionalProperties() {
//    @Bean
//    public final Properties additionalProperties() {
        final Properties hibernateProperties = new Properties();
        // Flush the DB at the end
//        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
//        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        hibernateProperties.setProperty("hibernate.show_sql", "true");
//        hibernateProperties.setProperty("hibernate.globally_quoted_identifiers", "true");
        return hibernateProperties;
    }

}