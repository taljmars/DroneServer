package com.dronedb.persistence.ws.internal;

import com.dronedb.persistence.scheme.SessionsSvcRemote;
import com.dronedb.persistence.services.SessionsSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

/**
 * Created by taljmars on 4/28/17.
 */
@Component
@WebService(endpointInterface = "com.dronedb.persistence.scheme.SessionsSvcRemote")
public class SessionsSvcRemoteImpl implements SessionsSvcRemote {

    @Autowired
    private SessionsSvc sessionsSvc;

    @Override
    public void publish() {
        sessionsSvc.publish();
    }

    @Override
    public void discard() {
        sessionsSvc.discard();
    }
}
