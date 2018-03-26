package com.db.server.security;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class ServerSessionRegistry {

    private final static Logger LOGGER = Logger.getLogger(ServerSessionRegistry.class);

    private Map<String /*Token/sessionID*/, MySessionInformation> mySessionInformationMap;

    @PostConstruct
    public void init() {
        mySessionInformationMap = new HashMap<>();
    }

    public void registerSession(String sessionId, SessionInformation sessionInformation) {
        LOGGER.debug("Register " + sessionId);
        mySessionInformationMap.put(sessionId, new MySessionInformation(sessionInformation));
    }

    public MySessionInformation unregisterSession(String sessionId) {
        LOGGER.debug("Unregister " + sessionId);
        MySessionInformation sessionInformation = mySessionInformationMap.get(sessionId);
        mySessionInformationMap.remove(sessionId);
        if (sessionInformation != null)
            sessionInformation.expireNow();
        return sessionInformation;
    }

    public String getSessionId(String userName) {
        Iterator<MySessionInformation> it = mySessionInformationMap.values().iterator();
        while (it.hasNext()) {
            MySessionInformation sessionInformation = it.next();
            String principalName = getPrincipalName(sessionInformation.getPrincipal());
            if (! principalName.equals(userName))
                continue;

            return sessionInformation.getSessionId();
        }
        return null;
    }

    public static String getPrincipalName(Object principal) {
        if (principal instanceof User)
            return ((User) principal).getUsername();

        if (principal instanceof String)
            return (String) principal;

        // TODO: change to better exception
        throw new RuntimeException("Unexpected principal");
    }

    public Iterable<String> getAllPrincipals() {
        List<String> principals = new ArrayList<>();
        for (MySessionInformation mySessionInformation : mySessionInformationMap.values()) {
            principals.add(getPrincipalName(mySessionInformation.getPrincipal()));
        }
        return principals;
    }

    public Iterable<? extends MySessionInformation> getAllSessions(String principal) {
        return Arrays.asList(mySessionInformationMap.get(principal));
    }

    public MySessionInformation getSessionInformation(String token) {
        return mySessionInformationMap.get(token);
    }
}

@Component
class MySessionRegistryImpl extends SessionRegistryImpl {

    private final static Logger LOGGER = Logger.getLogger(MySessionRegistryImpl.class);

    @Autowired private ServerSessionRegistry serverSessionRegistry;

    @Override
    public void registerNewSession(String sessionId, Object principal) {
        super.registerNewSession(sessionId, principal);
        serverSessionRegistry.registerSession(sessionId, getSessionInformation(sessionId));
    }
}
