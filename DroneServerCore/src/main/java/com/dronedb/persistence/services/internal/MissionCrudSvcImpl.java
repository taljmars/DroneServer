package com.dronedb.persistence.services.internal;

import com.dronedb.persistence.scheme.Mission;
import com.dronedb.persistence.scheme.MissionItem;
import com.dronedb.persistence.services.DroneDbCrudSvc;
import com.dronedb.persistence.services.MissionCrudSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by oem on 3/24/17.
 */
@Component
public class MissionCrudSvcImpl implements MissionCrudSvc {

    @Autowired DroneDbCrudSvc droneDbCrudSvc;

    @Override
    public Mission cloneMission(Mission mission) {

        List<UUID> newUid = new ArrayList<>();
        Mission clonedMission = mission.clone();
        for (UUID uid : clonedMission.getMissionItemsUids()) {
            MissionItem missionItem = droneDbCrudSvc.readByClass(uid, MissionItem.class);
            MissionItem cloneMissionItem = missionItem.clone();
            droneDbCrudSvc.update(cloneMissionItem);
            newUid.add(cloneMissionItem.getObjId());
        }

        clonedMission.setMissionItemsUids(newUid);
        droneDbCrudSvc.update(clonedMission);

        return clonedMission;
    }
}
