package com.db.aspects;

import com.db.persistence.events.audit.AccessEvent;
import com.db.persistence.scheme.LoginResponse;
import com.db.persistence.scheme.LogoutResponse;
import com.db.persistence.workSession.WorkSessionManager;
import com.db.server.security.MySessionInformation;
import com.db.server.security.ServerSessionRegistry;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;

@Aspect
public class LoginLogoutAspect {

    private final static Logger LOGGER = Logger.getLogger(LoginLogoutAspect.class);

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private ServerSessionRegistry serverSessionRegistry;

    @PostConstruct
    public void init() {
        LOGGER.debug("Initialized LoginLogoutAspect, EventPublisher=" + publisher + ", ServerSessionRegistry=" + serverSessionRegistry);
        if (publisher == null) {
            LOGGER.error("Event publisher is null, make sure you initialized the aspects via AspectFactory");
            System.exit(-1);
        }
    }

    @Transactional
    @AfterReturning(
            pointcut = "execution(* com.db.persistence.services.internal.LoginSvcImpl.login(..))",
            returning= "result")
    public void accessLogin(JoinPoint joinPoint, LoginResponse result) {
        try {
            LOGGER.debug("Publish Access event for token:" + result.getToken());
            publisher.publishEvent(new AccessEvent(AccessEvent.AccessEventType.LOGIN, result.getUserName(), result.getDate(), result.getReturnCode(), result.getMessage()));
        }
        catch (Throwable t) {LOGGER.error("Error occur during event publishing:" + t.getMessage(), t);}
    }

    @Transactional
    @AfterReturning(
            pointcut = "execution(* com.db.persistence.services.internal.LoginSvcImpl.logout(..))",
            returning= "result")
    public void accessLogout(JoinPoint joinPoint, LogoutResponse result) {
        try {
            LOGGER.debug("Publish Access event for token");
            publisher.publishEvent(new AccessEvent(AccessEvent.AccessEventType.LOGOUT, result.getUserName(), result.getDate(), result.getReturnCode(), result.getMessage()));
        }
        catch (Throwable t) {LOGGER.error("Error occur during event publishing:" + t.getMessage(), t);}
    }

}
