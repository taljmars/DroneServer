/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.server.aspectsInterceptors;

import com.db.aspects.LoginLogoutAspect;
import com.db.aspects.ObjectsModificationAspect;
import com.db.aspects.RegistrationAspect;
import com.db.persistence.cache.CacheLayerAspect;
import org.apache.log4j.Logger;
import org.aspectj.lang.Aspects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;

@Configuration
public class AspectsFactory {

    private final static Logger LOGGER = Logger.getLogger(AspectsFactory.class);

    @Autowired
    private ApplicationContext context;

    @PostConstruct
    public void init () {
        LOGGER.debug("Generating Aspects");
    }

    @Bean
	public ObjectsModificationAspect objectsModificationAspect() {
		ObjectsModificationAspect aspect = Aspects.aspectOf(ObjectsModificationAspect.class);
		return aspect;
	}

    @Bean
    public LoginLogoutAspect loginLogoutAspect() {
        LoginLogoutAspect aspect = Aspects.aspectOf(LoginLogoutAspect.class);
        return aspect;
    }

    @Bean
    public RegistrationAspect registrationAspect() {
        RegistrationAspect aspect = Aspects.aspectOf(RegistrationAspect.class);
        return aspect;
    }

    @Bean
    public CacheLayerAspect cacheLayerAspect() {
        CacheLayerAspect aspect = Aspects.aspectOf(CacheLayerAspect.class);
        return aspect;
    }
	
}
