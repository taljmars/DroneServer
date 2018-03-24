package com.db.persistence.services.internal;

import com.db.persistence.scheme.*;
import com.db.persistence.services.LoginSvc;
import com.db.persistence.workSession.WorkSession;
import com.db.persistence.workSession.WorkSessionManager;
import com.db.server.security.MySessionInformation;
import com.db.server.security.ServerSessionRegistry;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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


        // Build the all the relevant object and strcuture for the user
        WorkSession workSession;
        String userName = loginRequest.getUserName();
        String token = serverSessionRegistry.getSessionId(userName);
        if (token == null || token.isEmpty()){
            LOGGER.error("Failed to create session for user");
            resp.setMessage("Failed to create session for user");
            resp.setReturnCode(LoginLogoutStatus.FAIL);
            return resp;
        }

        if ((workSession = workSessionManager.getSessionByToken(token)) != null) {
            LOGGER.error("Already logged");
            resp.setMessage("Already logged");
            resp.setReturnCode(LoginLogoutStatus.FAIL);
            return resp;
        }

        if ((workSession = workSessionManager.getOrhpanSessionByUserName(userName)) != null) {
            LOGGER.debug("Found existing session, reviving it");
            workSessionManager.reviveSession(workSession, token);
        }
        else {
            LOGGER.debug("Create a new worksession for this login");
            workSessionManager.createSession(token, userName);
        }

        MySessionInformation sessionInformation = serverSessionRegistry.getSessionInformation(token);
        sessionInformation.setApplicationName(loginRequest.getApplicationName());
        sessionInformation.setTimeout(loginRequest.getTimeout());
        resp.setUserName(sessionInformation.getPrincipal());
        resp.setToken(token); // TODO: Should be a real token
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
            LOGGER.debug("Try to logout the server, token=" + getToken() + ", userName=" + sessionInformation.getPrincipal());
            sessionInformation = serverSessionRegistry.unregisterSession(getToken());
            workSessionManager.orphanizeSession(getToken());
            resp.setUserName(sessionInformation.getPrincipal());
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

    @Scheduled(fixedRate = 15 * 1000)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void tik() {
        try {
            Long currentTime = new Date().getTime();
            List<String> sessionIdsToFree = new ArrayList();
            Iterator<String> it = serverSessionRegistry.getAllPrincipals().iterator();
            while (it.hasNext()) {
                String principal = it.next();
                String sessionId = serverSessionRegistry.getSessionId(principal);
                MySessionInformation sessionInformation = serverSessionRegistry.getSessionInformation(sessionId);
                if (sessionInformation.getTimeout() == null) {
                    sessionInformation.setTimeout(DEFAULT_TIMEOUT);
                    continue;
                }
                Long timeSinceLastKA = (currentTime - sessionInformation.getLastRequest().getTime()) / 1000;
                LOGGER.debug(
                                "Past time: " + timeSinceLastKA +
                                ", timeout:" + sessionInformation.getTimeout() +
                                ", pricipal " + sessionInformation.getPrincipal() +
                                ", sessionId " + sessionInformation.getSessionId() +
                                ", CoreSessionId " + sessionInformation.getCoreObject().getSessionId()
                );
                if (timeSinceLastKA > sessionInformation.getTimeout()) {
                    //String token = sessionInformation.getSessionId();
                    // TODO: Need to investigate why there are some session that being stored in Spring session manager
                    // TODO: Even after I explicitly mark the session as expired
                    if (LOGGER.isDebugEnabled()) {
                        WorkSession workSession = workSessionManager.getSessionByToken(sessionId);
                        if (workSession == null || (workSession = workSessionManager.orphanizeSession(sessionId)) == null) {
                            LOGGER.warn("Failed to orphanized session, sessionId is a leftover");
                        } else {
                            LOGGER.debug("Session " + workSession.getSessionId() + " of user " + workSession.getUserName1() + " was timedout");
                        }
                    }
                    sessionIdsToFree.add(sessionId);
                }
            }
            for (String sessionId : sessionIdsToFree)
                serverSessionRegistry.unregisterSession(sessionId);
        }
        catch (Exception e) {
            LOGGER.error("Failed to check connections", e);
        }
    }
}
