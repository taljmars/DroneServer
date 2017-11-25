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
    @RequestMapping(value = "/publish")
//    @Transactional
    public void publish() {
        sessionsSvc.publish();
    }

    @Override
    @RequestMapping(value = "/publishForUser", method = RequestMethod.POST)
    public void publishForUser(@RequestParam String userName) {
        sessionsSvc.setForUser(userName);
        sessionsSvc.publish();
    }

        @Override
    @RequestMapping(value = "/discard")
//    @Transactional
    public void discard() {
        sessionsSvc.discard();
    }

    @Override
    @RequestMapping(value = "/discardForUser", method = RequestMethod.POST)
    public void discardForUser(@RequestParam String userName) {
        sessionsSvc.setForUser(userName);
        sessionsSvc.discard();
    }
}
