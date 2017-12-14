package com.db.server;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.dronedb.persistence" })
@ImportResource({"/com/dronedb/jpaConfig.xml"})
public class PersistenceJPAConfigXml {

    private final static Logger LOGGER = Logger.getLogger(PersistenceJPAConfigXml.class);

    public PersistenceJPAConfigXml() {
        super();
        LOGGER.debug("PersistenceJPAConfigXml Created");
    }

}