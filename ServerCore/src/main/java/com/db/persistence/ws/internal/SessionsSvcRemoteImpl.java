/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.persistence.ws.internal;

import com.db.persistence.services.SessionsSvc;
import com.db.persistence.ws.SessionsSvcRemote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionsSvcRemoteImpl implements SessionsSvcRemote {

    @Autowired
    private SessionsSvc sessionsSvc;

    @Override
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    public void publish() {
        sessionsSvc.publish();
    }

    @Override
    @RequestMapping(value = "/discard", method = RequestMethod.POST)
    public void discard() {
        sessionsSvc.discard();
    }
}
