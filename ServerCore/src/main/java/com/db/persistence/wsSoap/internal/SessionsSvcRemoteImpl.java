package com.db.persistence.wsSoap.internal;

import com.db.persistence.wsSoap.SessionsSvcRemote;
import com.db.persistence.services.SessionsSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

/**
 * Created by taljmars on 4/28/17.
 */
@Component
@WebService(endpointInterface = "com.db.persistence.wsSoap.SessionsSvcRemote")
//        , targetNamespace = "http://scheme.persistence.db.com/")
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
