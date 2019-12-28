package com.db.server.jpaVendorAdapters;

import org.apache.log4j.Logger;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;

import java.util.Properties;

import static com.db.server.SpringProfiles.Hibernate;

@Profile(Hibernate)
@Component
public class MyHibernateJpaVendorAdapter extends HibernateJpaVendorAdapter implements JpaVendorAdapterBase {

    private final static Logger LOGGER = Logger.getLogger(MyHibernateJpaVendorAdapter.class);

    @Autowired
    private String dialect;

    @Override
    public Properties getProperties() {
        LOGGER.debug("Generate Properties of Hibernate");

        final Properties properties = new Properties();
        // Flush the DB at the end
//        properties.setProperty(AvailableSettings.HBM2DDL_AUTO, "create-drop");
        properties.setProperty(AvailableSettings.HBM2DDL_AUTO, "update");
        properties.setProperty(AvailableSettings.DIALECT, dialect);
        properties.setProperty(AvailableSettings.SHOW_SQL, "false");

//        properties.setProperty(AvailableSettings.FORMAT_SQL, "true");
//        properties.setProperty(AvailableSettings.USE_SQL_COMMENTS, "true");
//        properties.setProperty("spring.jpa.properties.hibernate.type", "trace");

        properties.setProperty("spring.data.rest.detection-strategy", "annotated");

//        hibernateProperties.setProperty("hibernate.globally_quoted_identifiers", "true");

        return properties;
    }
}
