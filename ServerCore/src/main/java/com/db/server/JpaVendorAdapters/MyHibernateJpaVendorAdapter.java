package com.db.server.JpaVendorAdapters;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Qualifier(value = "hibernateJpaVendorAdapter")
@Component(value = "hibernateJpaVendorAdapter")
public class MyHibernateJpaVendorAdapter extends HibernateJpaVendorAdapter implements JpaVendorAdapterBase {

    private final static Logger logger = Logger.getLogger(MyHibernateJpaVendorAdapter.class);

    @Override
    public Properties getProperties() {
        logger.debug("Generate Properties of Hibernate");

        final Properties properties = new Properties();
        // Flush the DB at the end
//        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
//        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.show_sql", "true");
//        hibernateProperties.setProperty("hibernate.globally_quoted_identifiers", "true");
        return properties;
    }
}
