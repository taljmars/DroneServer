package com.dronedb.persistence.ws.internal;

import com.dronedb.persistence.scheme.MissionCrudSvcRemote;
import com.dronedb.persistence.scheme.Mission;
import com.dronedb.persistence.services.MissionCrudSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

/**
 * Created by oem on 3/24/17.
 */
@Component
@WebService(endpointInterface = "com.dronedb.persistence.scheme.MissionCrudSvcRemote")
public class MissionCrudSvcRemoteImpl implements MissionCrudSvcRemote {

    @Autowired MissionCrudSvc missionCrudSvc;

    @Override
    public Mission cloneMission(Mission mission) {
        return missionCrudSvc.cloneMission(mission);
    }
}
