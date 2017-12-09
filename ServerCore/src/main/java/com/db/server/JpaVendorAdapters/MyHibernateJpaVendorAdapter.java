package com.db.server.JpaVendorAdapters;

import ch.qos.logback.core.db.dialect.PostgreSQLDialect;
import org.apache.log4j.Logger;
//import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Qualifier(value = "hibernateJpaVendorAdapter")
@Component(value = "hibernateJpaVendorAdapter")
public class MyHibernateJpaVendorAdapter extends HibernateJpaVendorAdapter implements JpaVendorAdapterBase {

    private final static Logger LOGGER = Logger.getLogger(MyHibernateJpaVendorAdapter.class);

    @Override
    public Properties getProperties() {
        LOGGER.debug("Generate Properties of Hibernate");

        final Properties properties = new Properties();
        // Flush the DB at the end
//        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
//        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        properties.setProperty(AvailableSettings.HBM2DDL_AUTO, "create-drop");
        properties.setProperty(AvailableSettings.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty(AvailableSettings.SHOW_SQL, "true");
//        hibernateProperties.setProperty("hibernate.globally_quoted_identifiers", "true");
        return properties;
    }
}
