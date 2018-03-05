package com.db.persistence.wsRest.internal;

import com.db.persistence.services.SessionsSvc;
import com.db.persistence.wsRest.SessionsRestSvcRemote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by taljmars on 4/28/17.
 */
@RestController
public class SessionsRestSvcRemoteImpl implements SessionsRestSvcRemote {

    @Autowired
    private SessionsSvc sessionsSvc;

    @Override
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    public void publish(@RequestHeader("token") String token) {
        sessionsSvc.setToken(token);
        try {sessionsSvc.publish();}
        finally {sessionsSvc.flushToken();}
    }
    @Override
    @RequestMapping(value = "/discard", method = RequestMethod.POST)
    public void discard(@RequestHeader("token") String token) {
        sessionsSvc.setToken(token);
        try {sessionsSvc.discard();}
        finally {sessionsSvc.flushToken();}
    }
}
