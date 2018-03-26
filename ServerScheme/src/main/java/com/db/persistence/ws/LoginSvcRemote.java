package com.db.persistence.ws;

import com.db.persistence.scheme.KeepAliveResponse;
import com.db.persistence.scheme.LoginRequest;
import com.db.persistence.scheme.LoginResponse;
import com.db.persistence.scheme.LogoutResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface LoginSvcRemote {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRestRequest);

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<LogoutResponse> logout();

    @RequestMapping(value = "/keepAlive", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<KeepAliveResponse> keepAlive();

}
