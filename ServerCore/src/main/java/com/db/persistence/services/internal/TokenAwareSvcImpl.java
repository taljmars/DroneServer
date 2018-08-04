/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.persistence.services.internal;

import com.db.persistence.services.TokenAwareSvc;
import com.db.persistence.workSession.WorkSession;
import com.db.persistence.workSession.WorkSessionManager;
import com.db.server.security.MyToken;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.db.server.SecurityConfig.AUTH_TOKEN_KEY;
import static com.db.server.SecurityConfig.INTERNAL_SERVER_USER_TOKEN;

public abstract class TokenAwareSvcImpl implements TokenAwareSvc<MyToken> {

    private final static Logger LOGGER = Logger.getLogger(TokenAwareSvcImpl.class);

    @Autowired
    private WorkSessionManager workSessionManager;

    private MyToken token = null;

    @Override
    @Transactional
    public synchronized <T extends TokenAwareSvc> T setToken(MyToken token) {
        this.token = token;
        return (T) this;
    }

    @Override
    @Transactional
    public synchronized MyToken getToken() {
        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String uri = request.getRequestURI();
            if (uri.equals("/login") || uri.equals("/registerNewUser"))
                return INTERNAL_SERVER_USER_TOKEN;

            MyToken token = MyToken.deserialize(request.getHeader(AUTH_TOKEN_KEY));
            if (token != null)
                return token;
        }
        return this.token;
    }

    @Override
    @Transactional
    public synchronized WorkSession workSession() {
        if (RequestContextHolder.getRequestAttributes() != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String uri = request.getRequestURI();
            if (uri.equals("/login") || uri.equals("/registerNewUser"))
                return workSessionManager.getSessionByToken(INTERNAL_SERVER_USER_TOKEN);

            MyToken token = MyToken.deserialize(request.getHeader(AUTH_TOKEN_KEY));
            if (token != null)
                setToken(token);
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
