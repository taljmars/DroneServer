/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.persistence.ws.internal;

import com.db.persistence.scheme.RegistrationRequest;
import com.db.persistence.scheme.RegistrationResponse;
import com.db.persistence.services.RegistrationSvc;
import com.db.persistence.ws.RegistrationSvcRemote;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegistrationSvcRemoteImpl implements RegistrationSvcRemote {

    private final static Logger LOGGER = Logger.getLogger(RegistrationSvcRemoteImpl.class);

    @Autowired
    private RegistrationSvc registrationSvc;

    @Override
    @RequestMapping(value = "/registerNewUser", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<RegistrationResponse> registerNewUser(@RequestBody RegistrationRequest registrationRequest) {
        RegistrationResponse res = registrationSvc.registerNewUser(registrationRequest);
        return new ResponseEntity(res, HttpStatus.OK);
    }

}
