/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.persistence.services.internal;

import com.db.persistence.services.TokenAwareSvc;
import com.db.persistence.workSession.WorkSession;
import com.db.persistence.workSession.WorkSessionManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.db.persistence.workSession.Constant.INTERNAL_SERVER_USER_TOKEN;
import static com.db.server.SecurityConfig.AUTH_TOKEN_KEY;

public abstract class TokenAwareSvcImpl<T extends TokenAwareSvc> implements TokenAwareSvc {

    private final static Logger LOGGER = Logger.getLogger(TokenAwareSvcImpl.class);

    @Autowired
    private WorkSessionManager workSessionManager;

    private String token = null;

    @Override
    @Transactional
    public T setToken(String token) {
        this.token = token;
        return (T) this;
    }

    @Override
    @Transactional
    public String getToken() {
        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String uri = request.getRequestURI();
            if (uri.equals("/login") || uri.equals("/registerNewUser"))
                return INTERNAL_SERVER_USER_TOKEN;

            String token = request.getHeader(AUTH_TOKEN_KEY);
            if (token != null)
                return token;
        }
        return this.token;
    }

    @Override
    @Transactional
    public void flushToken() {
        this.token = null;
    }

    @Override
    @Transactional
    public WorkSession workSession() {
        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String uri = request.getRequestURI();
            if (uri.equals("/login") || uri.equals("/registerNewUser"))
                return workSessionManager.getSessionByToken(INTERNAL_SERVER_USER_TOKEN);

            String token = request.getHeader(AUTH_TOKEN_KEY);
            if (token != null)
                this.token = token;
        }

        LOGGER.debug("Context was changed for token : " + this.token + ", For service: " + getClass().getSimpleName());
        WorkSession existingWorkSession = workSessionManager.getSessionByToken(this.token);
        if (existingWorkSession == null) {
            // TODO: throw normal exception
            throw new RuntimeException("Session doesn't exist, re-login to the system");
        }

        return existingWorkSession;
    }

}
