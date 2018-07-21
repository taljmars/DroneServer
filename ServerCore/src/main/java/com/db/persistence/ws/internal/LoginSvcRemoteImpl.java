/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.persistence.ws.internal;

import com.db.persistence.scheme.KeepAliveResponse;
import com.db.persistence.scheme.LoginRequest;
import com.db.persistence.scheme.LoginResponse;
import com.db.persistence.scheme.LogoutResponse;
import com.db.persistence.services.LoginSvc;
import com.db.persistence.ws.LoginSvcRemote;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginSvcRemoteImpl implements LoginSvcRemote {

    private final static Logger LOGGER = Logger.getLogger(LoginSvcRemoteImpl.class);

    @Autowired
    private LoginSvc loginSvc;

    @Override
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRestRequest) {
        loginRestRequest.setUserName((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        LoginResponse res = loginSvc.login(loginRestRequest);
        return new ResponseEntity(res, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<LogoutResponse> logout() {
        LogoutResponse res = loginSvc.logout();
        return new ResponseEntity(res, HttpStatus.OK);
    }

    @Override
    @RequestMapping(value = "/keepAlive", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<KeepAliveResponse> keepAlive() {
        KeepAliveResponse res = loginSvc.keepAlive();
        return new ResponseEntity(res, HttpStatus.OK);
    }
}
