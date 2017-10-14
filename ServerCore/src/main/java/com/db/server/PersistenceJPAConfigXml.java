package com.db.server;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.dronedb.persistence" })
@ImportResource({"/com/dronedb/jpaConfig.xml"})
public class PersistenceJPAConfigXml {

    public PersistenceJPAConfigXml() {
        super();
        System.out.println("PersistenceJPAConfigXml Created");
    }

}