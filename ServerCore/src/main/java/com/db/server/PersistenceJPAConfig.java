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

import com.db.server.JpaVendorAdapters.JpaVendorAdapterBase;
import com.plugins_manager.PluginsManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan({ "com.dronedb.persistence", "com.db.persistence" })
@ComponentScan({ "com.db.server" })
@ComponentScan({ "com.db.persistence.wsRest.internal" })
public class PersistenceJPAConfig {

    private static final String FILENAME = "PASS_FILE";

    private final static Logger logger = Logger.getLogger(DroneServer.class);

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

//    @Bean
//    @Lazy
//    public EntityManager createEntityManager(@Autowired EntityManagerFactory entityManagerFactory) {
//        return SharedEntityManagerCreator.createSharedEntityManager(entityManagerFactory);
//    }

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
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Autowired DataSource dataSource,
//            @Autowired @Qualifier("hibernateJpaVendorAdapter") JpaVendorAdapterBase jpaVendorAdapter)
            @Autowired @Qualifier("eclipseLinkJpaVendorAdapter") JpaVendorAdapterBase jpaVendorAdapter)
    {
        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        logger.debug("Set persistence data source: " + dataSource);
        em.setDataSource(dataSource);

        logger.debug("Fetching plugins manager");
        PluginsManager pluginsManager = PluginsManager.getInstance();

        logger.debug("Loading Scheme to DB");
        List<String> schemesLocation = pluginsManager.getSchemes();
        schemesLocation.stream().forEach((scmName) -> logger.debug("Scheme target name: " + scmName));
        String[] schemes = new String[schemesLocation.size()];
        schemes = schemesLocation.toArray(schemes);

        logger.debug("Scan for schemes");
        em.setPackagesToScan(schemes);

        // Setting JPA adapter and it properties
        logger.debug("Setting JPA adapter: " + jpaVendorAdapter.getClass().getCanonicalName());
        em.setJpaVendorAdapter(jpaVendorAdapter);
        logger.debug("Setting JPA properties: " + jpaVendorAdapter.getProperties());
        em.setJpaProperties(jpaVendorAdapter.getProperties());

        //TODO: Remove this limitation, we should enable auto-validate in the future
        // Prevent validation from automatic running during publish by hibernate
        em.setValidationMode(ValidationMode.NONE);

        return em;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}