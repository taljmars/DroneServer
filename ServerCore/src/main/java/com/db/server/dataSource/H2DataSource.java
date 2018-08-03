package com.db.server.dataSource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import static com.db.server.SpringProfiles.H2;

@Profile(H2)
@Configuration
public class H2DataSource implements DataSourceBase {

    /**
     * You can always see the db in the following link during runtime:
     * http://localhost:8080/console
     */

    private final static Logger LOGGER = Logger.getLogger(H2DataSource.class);

    @Bean
    @Override
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:drone;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    @Override
    public String dialect() {
        return "org.hibernate.dialect.H2Dialect";
    }
}