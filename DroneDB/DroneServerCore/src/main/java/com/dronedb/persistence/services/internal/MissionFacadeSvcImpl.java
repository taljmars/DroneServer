package com.dronedb.persistence.services.internal;

import com.dronedb.persistence.scheme.BaseObject;
import com.dronedb.persistence.scheme.Mission;
import com.dronedb.persistence.scheme.MissionItem;
import com.dronedb.persistence.services.DroneDbCrudSvc;
import com.dronedb.persistence.services.MissionFacadeSvc;
import com.dronedb.persistence.services.QuerySvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by taljmars on 3/18/17.
 */
@Component
public class MissionFacadeSvcImpl implements MissionFacadeSvc {

    @Autowired
    DroneDbCrudSvc droneDbCrudSvc;

    @Autowired
    QuerySvc querySvc;

    @Override
    public Mission write(Mission mission) {
        for (MissionItem missionItem : mission.getMissionItems()) {
            droneDbCrudSvc.update(missionItem);
        }
        return droneDbCrudSvc.update(mission);
    }

    @Override
    public void delete(Mission mission) {
        droneDbCrudSvc.delete(mission);
    }

    @Override
    public Set<Mission> getAll() {
        return new HashSet<Mission>((List<Mission>) querySvc.runNamedQuery("GetAllMissions", Mission.class));
    }
}
