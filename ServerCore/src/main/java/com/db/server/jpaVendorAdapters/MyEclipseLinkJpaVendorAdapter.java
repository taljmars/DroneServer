package com.db.server.jpaVendorAdapters;

import org.apache.log4j.Logger;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.logging.SessionLog;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Primary
@Qualifier(value = "eclipseLinkJpaVendorAdapter")
@Component(value = "eclipseLinkJpaVendorAdapter")
public class MyEclipseLinkJpaVendorAdapter extends EclipseLinkJpaVendorAdapter implements JpaVendorAdapterBase {

    private final static Logger LOGGER = Logger.getLogger(MyEclipseLinkJpaVendorAdapter.class);

    @Override
    public Properties getProperties() {
        LOGGER.debug("Generate Properties of EclipseLink");
        final Properties properties = new Properties();
        properties.setProperty(PersistenceUnitProperties.WEAVING, Boolean.FALSE.toString() /*"false"*/);
//        properties.setProperty(PersistenceUnitProperties.BATCH_WRITING, BatchWriting.JDBC);
        properties.setProperty(PersistenceUnitProperties.LOGGING_LEVEL, SessionLog.ALL_LABEL/*FINE_LABEL*/);

        properties.setProperty(PersistenceUnitProperties.JDBC_DRIVER, "org.postgresql.Driver");
        properties.setProperty(PersistenceUnitProperties.JDBC_URL, "jdbc:postgresql://localhost:5432/dronedb");
        properties.setProperty(PersistenceUnitProperties.JDBC_USER, "postgres");
        properties.setProperty(PersistenceUnitProperties.JDBC_PASSWORD, "postgres");
        properties.setProperty(PersistenceUnitProperties.DDL_GENERATION, PersistenceUnitProperties.DROP_AND_CREATE);
//        properties.setProperty(PersistenceUnitProperties.PERSISTENCE_CONTEXT_FLUSH_MODE, FlushModeType.COMMIT.toString());
        properties.setProperty(PersistenceUnitProperties.PERSISTENCE_CONTEXT_PERSIST_ON_COMMIT, "false");

        properties.setProperty("showSql", "true");
//        properties.setProperty("stringtype", "String");

        return properties;
    }
}
