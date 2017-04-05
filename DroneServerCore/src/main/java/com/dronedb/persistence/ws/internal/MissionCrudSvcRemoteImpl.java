package com.dronedb.persistence.ws.internal;

import com.dronedb.persistence.exception.DatabaseValidationException;
import com.dronedb.persistence.scheme.DatabaseRemoteValidationException;
import com.dronedb.persistence.scheme.MissionCrudSvcRemote;
import com.dronedb.persistence.scheme.Mission;
import com.dronedb.persistence.scheme.MissionItem;
import com.dronedb.persistence.services.MissionCrudSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

/**
 * Created by taljmars on 3/24/17.
 */
@Component
@WebService(endpointInterface = "com.dronedb.persistence.scheme.MissionCrudSvcRemote")
public class MissionCrudSvcRemoteImpl implements MissionCrudSvcRemote {

    @Autowired MissionCrudSvc missionCrudSvc;

    @Override
    public Mission cloneMission(Mission mission) throws DatabaseRemoteValidationException {
        try {
            return missionCrudSvc.cloneMission(mission);
        }
        catch (DatabaseValidationException e) {
            throw new DatabaseRemoteValidationException(e.getMessage());
        }
    }

    @Override
    public <T extends MissionItem> T createMissionItem(Class<T> clz) {
        return missionCrudSvc.createMissionItem(clz);
    }

    @Override
    public Mission createMission() {
        return missionCrudSvc.createMission();
    }
}
