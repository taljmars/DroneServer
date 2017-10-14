package com.db.persistence.wsRest.internal;

import com.db.persistence.services.SessionsSvc;
import com.db.persistence.wsRest.SessionsRestSvcRemote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by taljmars on 4/28/17.
 */
@RestController
public class SessionsRestSvcRemoteImpl implements SessionsRestSvcRemote {

    @Autowired
    private SessionsSvc sessionsSvc;

    @RequestMapping(value = "/publish")
    public void publish() {
        sessionsSvc.publish();
    }

    @RequestMapping(value = "/discard")
    public void discard() {
        sessionsSvc.discard();
    }
}
