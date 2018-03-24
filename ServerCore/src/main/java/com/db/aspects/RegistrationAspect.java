/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.aspects;

import com.db.persistence.events.audit.RegistrationEvent;
import com.db.persistence.scheme.RegistrationResponse;
import com.db.server.security.ServerSessionRegistry;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Aspect
public class RegistrationAspect {

    private final static Logger LOGGER = Logger.getLogger(RegistrationAspect.class);

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private ServerSessionRegistry serverSessionRegistry;

    @PostConstruct
    public void init() {
        LOGGER.debug("Initialized RegistrationAspect, EventPublisher=" + publisher);
        if (publisher == null) {
            LOGGER.error("Event publisher is null, make sure you initialized the aspects via AspectFactory");
            System.exit(-1);
        }
    }

    @Transactional
    @AfterReturning(
            pointcut = "execution(* com.db.persistence.services.internal.RegistrationSvcImpl.registerNewUser(..))",
            returning= "result")
    public void registerNewUser(JoinPoint joinPoint, RegistrationResponse result) {
        try {
            if (result.getReturnCode() != 0)
                return;

            LOGGER.debug("Publish Access event for user creation:" + result.getUserName());
            publisher.publishEvent(new RegistrationEvent(RegistrationEvent.AccessEventType.USERCREATION, result.getUserName(), result.getDate(), result.getReturnCode(), result.getMessage()));
        }
        catch (Throwable t) {LOGGER.error("Error occur during event publishing:" + t.getMessage(), t);}
    }

}
