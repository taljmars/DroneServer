package com.dronedb.persistence.services.internal;

import com.dronedb.persistence.exception.DatabaseValidationException;
import com.dronedb.persistence.exception.ObjectInstanceException;
import com.dronedb.persistence.scheme.Mission;
import com.dronedb.persistence.scheme.MissionItem;
import com.dronedb.persistence.services.DroneDbCrudSvc;
import com.dronedb.persistence.services.MissionCrudSvc;
import javassist.tools.rmi.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by taljmars on 3/24/17.
 */
@Component
public class MissionCrudSvcImpl implements MissionCrudSvc {

    @Autowired DroneDbCrudSvc droneDbCrudSvc;

    /**
     * Will clone the mission object and every mission item.
     * Mind the the objid of each object and subobject is being regenerated
     * @param mission
     * @return
     * @throws DatabaseValidationException
     */
    @Override
    public Mission cloneMission(Mission mission) throws DatabaseValidationException, ObjectNotFoundException, ObjectInstanceException {

        List<UUID> newUid = new ArrayList<>();
        Mission clonedMission = mission.clone();
        for (UUID uid : clonedMission.getMissionItemsUids()) {
            MissionItem missionItem = droneDbCrudSvc.readByClass(uid, MissionItem.class);
            MissionItem cloneMissionItem = missionItem.clone();
            cloneMissionItem = droneDbCrudSvc.update(cloneMissionItem);
            newUid.add(cloneMissionItem.getKeyId().getObjId());
        }

        clonedMission.setMissionItemsUids(newUid);
        clonedMission = droneDbCrudSvc.update(clonedMission);

        return clonedMission;
    }

    @Override
    public Mission createMission() throws ObjectInstanceException {
        return droneDbCrudSvc.create(Mission.class);
    }

    @Override
    public <T extends MissionItem> T createMissionItem(Class<T> clz) throws ObjectInstanceException {
        return (T) droneDbCrudSvc.create(clz);
    }
}
