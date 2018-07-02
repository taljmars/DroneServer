package com.db.server.security;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ServerSessionRegistry {

    private final static Logger LOGGER = Logger.getLogger(ServerSessionRegistry.class);

    private Map<String /*Token/sessionID*/, MySessionInformation> mySessionInformationMap;

    @Autowired
    private MyTokenService tokenService;

    @PostConstruct
    public void init() {
        mySessionInformationMap = new HashMap<>();
    }

    public void registerSession(String sessionId, MySessionInformation sessionInformation) {
        LOGGER.debug("Register " + sessionId);
        mySessionInformationMap.put(sessionId, sessionInformation);
    }

    public MySessionInformation unregisterSession(String sessionId) {
        LOGGER.debug("Unregister " + sessionId);
        MySessionInformation sessionInformation = mySessionInformationMap.get(sessionId);
        mySessionInformationMap.remove(sessionId);
        if (sessionInformation != null && !sessionInformation.isExpired())
            sessionInformation.expireNow();

        tokenService.expire(sessionId);
        return sessionInformation;
    }

    public MySessionInformation getSessionInformation(String token) {
        return mySessionInformationMap.get(token);
    }

    public List<String> expireNow() {
        List<String> res = new ArrayList<>();
        for (MySessionInformation sessionInformation : mySessionInformationMap.values()) {
            sessionInformation.expireNow();
            if (sessionInformation.isExpired()) {
                LOGGER.debug("Session Timed out ! -> " + sessionInformation);
                res.add(sessionInformation.getToken());
            }
        }
        return res;
    }
}