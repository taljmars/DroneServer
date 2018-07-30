/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.persistence.services.internal;

import com.db.persistence.exception.QueryException;
import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.RegistrationRequest;
import com.db.persistence.scheme.RegistrationResponse;
import com.db.persistence.scheme.User;
import com.db.persistence.services.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RegistrationSvcImpl extends TokenAwareSvcImpl implements RegistrationSvc {

    private final static Logger LOGGER = Logger.getLogger(RegistrationSvcImpl.class);

    @Autowired
    private QuerySvc querySvc;

    @Autowired
    private ObjectCrudSvc objectCrudSvc;

    @Autowired
    private SessionsSvc sessionsSvc;

    @Override
    @Transactional
    public RegistrationResponse registerNewUser(RegistrationRequest registrationRequest) {
        LOGGER.debug("Try to register new user " + registrationRequest.toString());
        RegistrationResponse resp = new RegistrationResponse();
        resp.setDate(new Date());
        String userName = registrationRequest.getUserName();
        String password = registrationRequest.getPassword();
        resp.setUserName(userName);

        if (!isUserNameValid(userName)) {
            LOGGER.debug("User name invalid " + userName);
            resp.setMessage("User name invalid, make sure it contain at lease 4 characters and not space");
            resp.setReturnCode(-1);
            return resp;
        }

        if (!isPasswordValid(userName, password)) {
            LOGGER.debug("Password invalid " + password);
            resp.setMessage("Password invalid, make sure it contain at lease 4 characters and not space");
            resp.setReturnCode(-1);
            return resp;
        }

        try {
            LOGGER.debug("Checking user " + userName + " doesn't exist");
            User user = getUserByName(userName);
            if (user != null) {
                LOGGER.debug("User already exist");
                resp.setMessage("User already exist");
                resp.setReturnCode(-1);
                return resp;
            }

            LOGGER.debug("Creating user in database");
            user = objectCrudSvc.create(User.class.getCanonicalName());
            user.setUserName(userName);
            user.setPassword(password);
            objectCrudSvc.update(user);

            LOGGER.debug("publishing new user");
            sessionsSvc.publish();
        }
        catch (Exception e) {
            LOGGER.error("Failed to create user", e);
            resp.setMessage("Internal Error: Failed to create user");
            resp.setReturnCode(-1);
            return resp;
        }

        LOGGER.debug("User '" + userName + "' was successfully created");
        resp.setMessage("User was successfully created");
        resp.setReturnCode(0);
        return resp;
    }

    @Override
    public User getUserByName(String userName) {
        try {
            if (!isUserNameValid(userName))
                return null;

            QueryRequest queryRequest = new QueryRequest();
            queryRequest.setQuery("GetUserByName");
            queryRequest.setClz(User.class);
            Map<String, String> params = new HashMap<>();
            params.put("USERNAME", userName);
            queryRequest.setParameters(params);
            List<? extends BaseObject> res = querySvc.query(queryRequest);
            if (res == null || res.isEmpty())
                return null;

            return (User) res.get(0);
        }
        catch (QueryException e) {
            throw new RuntimeException("Failed to get user", e);
        }
    }

    private boolean isUserNameValid(String userName) {
        if (userName == null)
            return false;

        if (userName.contains(" "))
            return false;

        return userName.length() >= 4;
    }

    private boolean isPasswordValid(String userName, String password) {
        if (password == null)
            return false;

        if (password.contains(" "))
            return false;

        return password.length() >= 4;
    }
}
