package com.dronedb.persistence.ws.internal;

import com.dronedb.persistence.scheme.Mission;
import com.dronedb.persistence.services.MissionFacadeSvc;
import com.dronedb.persistence.ws.MissionFacadeRemote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.Set;

/**
 * Created by taljmars on 3/18/17.
 */
@Component
@WebService(endpointInterface = "com.dronedb.persistence.ws.MissionFacadeRemote")
public class MissionFacadeRemoteImpl implements MissionFacadeRemote {

    @Autowired
    private MissionFacadeSvc missionFacadeSvc;

    @PostConstruct
    public void init() {
        Assert.notNull(missionFacadeSvc,"Failed to initiate 'missionFacadeSvc'");
    }

    @Override
    public Mission write(@WebParam Mission mission) {
        return missionFacadeSvc.write(mission);
    }

    @Override
    public void delete(@WebParam Mission mission) {
        missionFacadeSvc.delete(mission);
    }
}
