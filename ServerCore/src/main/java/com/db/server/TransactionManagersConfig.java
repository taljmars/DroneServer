package com.db.server;

import com.db.persistence.objectStore.MyJpaTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Created by taljmars on 3/22/17.
 */
@Configuration
@EnableTransactionManagement
public class TransactionManagersConfig {

    @Autowired
    EntityManagerFactory emf;

    @Bean
    public PlatformTransactionManager transactionManager(@Autowired DataSource dataSource) {
        JpaTransactionManager tm = new MyJpaTransactionManager();
        tm.setEntityManagerFactory(emf);
        tm.setDataSource(dataSource);
        return tm;
    }

}
