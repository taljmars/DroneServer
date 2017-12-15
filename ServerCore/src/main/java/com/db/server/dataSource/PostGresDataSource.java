package com.db.server.dataSource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Configuration
public class PostGresDataSource implements DataSourceBase {

    private static final String FILENAME = "PASS_FILE";

    private final static Logger LOGGER = Logger.getLogger(PostGresDataSource.class);

    public PostGresDataSource() {
        LOGGER.debug("PostGresDataSource Created");
    }

    private static String getPassword() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILENAME));
            return br.readLine();
        }
        catch (FileNotFoundException e) {
            LOGGER.error("Password file doesn't exist");
        }
        catch (IOException e) {
            LOGGER.error("Failed to read password file");
        }
        //TODO: TALMA WA
        return "postgres";
//        return null;
    }

    @Bean
    @Override
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
}