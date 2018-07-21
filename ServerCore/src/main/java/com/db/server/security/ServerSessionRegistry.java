package com.db.server.security;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ServerSessionRegistry {

    private final static Logger LOGGER = Logger.getLogger(ServerSessionRegistry.class);

    private Map<MyToken /*Token/sessionID*/, MySessionInformation> mySessionInformationMap;

    @Autowired
    private MyTokenService tokenService;

    @Autowired
    private ApplicationContext applicationContext;

    private Integer sessionLimitation;

    @PostConstruct
    public void init() {
        mySessionInformationMap = new HashMap<>();
        sessionLimitation = (Integer) applicationContext.getBean("sessionLimitation");
    }

    public void registerSession(MyToken token, MySessionInformation sessionInformation) {
        LOGGER.debug("Register " + token);
        if (mySessionInformationMap.keySet().size() >= sessionLimitation)
            throw new RuntimeException("Too many sessions");

        mySessionInformationMap.put(token.initializeNow(), sessionInformation);
    }

    public MySessionInformation unregisterSession(MyToken token) {
        LOGGER.debug("Unregister " + token);
        MySessionInformation sessionInformation = mySessionInformationMap.get(token);
        mySessionInformationMap.remove(token);
        if (sessionInformation != null && !sessionInformation.isExpired())
            sessionInformation.expireNow();

        tokenService.revoke(token);
        return sessionInformation;
    }

    public MySessionInformation getSessionInformation(MyToken token) {
        return mySessionInformationMap.get(token);
    }

    public List<MyToken> expireNow() {
        List<MyToken> res = new ArrayList<>();
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