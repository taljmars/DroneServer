package com.db.persistence.ws;

import com.db.persistence.scheme.RegistrationRequest;
import com.db.persistence.scheme.RegistrationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public interface RegistrationSvcRemote {

    @RequestMapping(value = "/registerNewUser", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<RegistrationResponse> registerNewUser(@RequestBody RegistrationRequest registrationRequest);

}
