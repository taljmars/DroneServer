/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.persistence.services.internal;

import com.db.persistence.scheme.*;
import com.db.persistence.services.LoginSvc;
import com.db.persistence.workSession.WorkSession;
import com.db.persistence.workSession.WorkSessionManager;
import com.db.server.security.MySessionInformation;
import com.db.server.security.MyToken;
import com.db.server.security.ServerSessionRegistry;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.db.persistence.scheme.LoginLogoutStatus.FAIL;
import static com.db.persistence.scheme.LoginLogoutStatus.OK;
import static com.db.server.security.MySessionInformation.DEFAULT_TIMEOUT;

@Component
public class LoginSvcImpl extends TokenAwareSvcImpl implements LoginSvc {

    private final static Logger LOGGER = Logger.getLogger(LoginSvcImpl.class);

    @Autowired
    private ServerSessionRegistry serverSessionRegistry;

    @Autowired
    private WorkSessionManager workSessionManager;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        LOGGER.debug("Try to login the server " + loginRequest.toString());
        LoginResponse resp = new LoginResponse();
        resp.setDate(new Date());

        // Validating Request
        if (loginRequest.getUserName() == null || loginRequest.getUserName().isEmpty()) {
            LOGGER.error("Illegal request - missing parameters");
            resp.setMessage("Illegal request");
            resp.setReturnCode(LoginLogoutStatus.FAIL);
            return resp;
        }

        // Build the all the relevant object and structure for the user
        WorkSession workSession;
        String userName = loginRequest.getUserName();
        MyToken token = (MyToken) SecurityContextHolder.getContext().getAuthentication().getDetails();//serverSessionRegistry.getSessionId(userName);
        if (token == null) {
            LOGGER.error("Failed to create session for user");
            resp.setMessage("Failed to create session for user");
            resp.setReturnCode(LoginLogoutStatus.FAIL);
            return resp;
        }

        if ((workSession = workSessionManager.getOrhpanSessionByUserName(userName)) != null) {
            LOGGER.debug("Found existing session, reviving it");
            workSession = workSessionManager.reviveSession(workSession, token);
        }
        else {
            LOGGER.debug("Create a new worksession for this login");
            workSession = workSessionManager.createSession(token, userName);
        }

        MySessionInformation sessionInformation = new MySessionInformation();
        sessionInformation.setUserName(userName);
        sessionInformation.setApplicationName(loginRequest.getApplicationName());
        sessionInformation.setTimeout(loginRequest.getTimeout());
        sessionInformation.setToken(token);

        try {
            serverSessionRegistry.registerSession(token, sessionInformation);
        }
        catch (Exception e) {
            resp.setMessage("Login failed, " + e.getMessage());
            resp.setReturnCode(FAIL);
            LOGGER.debug("Logged failed !, user=" + loginRequest.getUserName());
            return resp;
        }

        resp.setUserName(sessionInformation.getUserName());
        resp.setToken(token.serialize());
        resp.setMessage("Login successfully");
        resp.setReturnCode(OK);
        LOGGER.debug("Logged successfully !, user=" + loginRequest.getUserName() + " ,token=" + resp.getToken());
        return resp;
    }

    @Override
    @Transactional
    public LogoutResponse logout() {
        LogoutResponse resp = new LogoutResponse();
        resp.setDate(new Date());
        MySessionInformation sessionInformation = serverSessionRegistry.getSessionInformation(getToken());
        if (sessionInformation == null) {
            LOGGER.error("Try to logout the server, session doesn't exist " + getToken());
            resp.setMessage("Failed to logout");
            resp.setReturnCode(FAIL);
        }
        else {
            LOGGER.debug("Try to logout the server, token=" + getToken() + ", userName=" + sessionInformation.getUserName());
            sessionInformation = serverSessionRegistry.unregisterSession(getToken());
            workSessionManager.orphanizeSession(getToken());
            resp.setUserName(sessionInformation.getUserName());
            resp.setMessage("Successfully Logout");
            resp.setReturnCode(OK);
        }

        return resp;
    }

    @Override
    @Transactional
    public KeepAliveResponse keepAlive() {
        KeepAliveResponse res = new KeepAliveResponse();
        MySessionInformation sessionInformation = serverSessionRegistry.getSessionInformation(getToken());
        if (sessionInformation == null) {
            res.setMessage("Session is already dead");
            res.setReturnCode(FAIL);
        }
        else {
            sessionInformation.refreshLastRequest();
            res.setMessage("Ping successfully");
            res.setServerDate(new Date());
            res.setReturnCode(OK);
        }
        LOGGER.debug("Keep alive was received");
        return res;
    }

    @Scheduled(fixedRate = 60 * 1000) // 30 Seconds
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void tik() {
        LOGGER.info("=============================================================================");
        LOGGER.info("========================= LOGIN SESSION EXPIRATION ==========================");
        try {
            LOGGER.debug("Searching for expired sessions");
            List<MyToken> expiredTokens = serverSessionRegistry.expireNow();
            LOGGER.debug("There are " + expiredTokens.size() + " session that needs to be expired");
            Iterator<MyToken> it = expiredTokens.iterator();
            while (it.hasNext()) {
                MyToken token = it.next();
                WorkSession workSession = workSessionManager.getSessionByToken(token);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("--> Expiring token: " + token + ", Session: " + workSession);
                }
                if (workSession == null || (workSession = workSessionManager.orphanizeSession(token)) == null) {
                    LOGGER.warn("Failed to orphanized session, sessionId is a leftover");
                } else {
                    LOGGER.debug("Session " + workSession.getSessionId() + " of user " + workSession.getUserName() + " was timed out");
                }
                serverSessionRegistry.unregisterSession(token);
            }
        }
        catch (Exception e) {
            LOGGER.error("Failed to check connections", e);
        }
        LOGGER.info("=============================================================================");
    }
}
