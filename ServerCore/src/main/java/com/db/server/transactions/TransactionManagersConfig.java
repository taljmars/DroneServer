package com.db.server.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.aspectj.AnnotationTransactionAspect;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Created by taljmars on 3/22/17.
 */
@Configuration
@EnableTransactionManagement//(mode = AdviceMode.ASPECTJ)
public class TransactionManagersConfig {

    @Autowired
    private EntityManagerFactory emf;

    @Bean
    public PlatformTransactionManager jpaTransactionManager(@Autowired DataSource dataSource) {
        JpaTransactionManager tm = new MyJpaTransactionManager();
        tm.setEntityManagerFactory(emf);
        tm.setDataSource(dataSource);
        return tm;
    }

//    @Bean
    public PlatformTransactionManager datasourceTransactionManager(@Autowired DataSource dataSource) {
        MyDataSourceTransactionManager tm = new MyDataSourceTransactionManager();
        tm.setDataSource(dataSource);
        return tm;
    }

    @Bean
    public AnnotationTransactionAspect annotationTransactionAspect(@Autowired PlatformTransactionManager ptm) {
        AnnotationTransactionAspect annotationTransactionAspect = AnnotationTransactionAspect.aspectOf();
        annotationTransactionAspect.setTransactionManager(ptm);
        return annotationTransactionAspect;
    }

}
