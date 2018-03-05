package com.db.persistence.services.internal;

import com.db.persistence.services.TokenAwareSvc;
import com.db.persistence.workSession.WorkSession;
import com.db.persistence.workSession.WorkSessionManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public abstract class TokenAwareSvcImpl implements TokenAwareSvc {

    private final static Logger LOGGER = Logger.getLogger(TokenAwareSvcImpl.class);

    private String currentToken = "";

    @Autowired
    private WorkSessionManager workSessionManager;

    @Override
    @Transactional
    public void setToken(String token) {
        currentToken = token;
        LOGGER.debug("Context was changed for token : " + currentToken);
        WorkSession existingWorkSession = workSessionManager.getSessionByToken(currentToken);
        if (existingWorkSession == null) {
            // TODO: throw normal exception
            throw new RuntimeException("Session doesn't exist, re-login to the system");
        }
        workSession = existingWorkSession;
    }

    @Override
    @Transactional
    public String getToken() {
        return currentToken;
    }

    @Override
    @Transactional
    public void flushToken() {
        currentToken = null;
    }

    protected WorkSession workSession;

}
