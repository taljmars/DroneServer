package com.dronedb.persistence.services.internal;

import com.db.persistence.exception.DatabaseValidationException;
import com.db.persistence.exception.ObjectInstanceException;
import com.dronedb.persistence.services.MissionCrudSvc;
import com.db.persistence.services.ObjectCrudSvc;
import com.dronedb.persistence.scheme.Mission;
import com.dronedb.persistence.scheme.MissionItem;
import javassist.tools.rmi.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taljmars on 3/24/17.
 */
@Component
public class MissionCrudSvcImpl implements MissionCrudSvc {

    @Autowired
    ObjectCrudSvc objectCrudSvc;

    /**
     * Will clone the mission object and every mission item.
     * Mind the the objid of each object and subobject is being regenerated
     * @param mission
     * @return
     * @throws DatabaseValidationException
     */
    @Override
    public Mission cloneMission(Mission mission) throws DatabaseValidationException, ObjectNotFoundException, ObjectInstanceException {
        List<String> newUid = new ArrayList<>();
        Mission clonedMission = mission.clone();
        for (String uid : clonedMission.getMissionItemsUids()) {
            MissionItem missionItem = objectCrudSvc.readByClass(uid, MissionItem.class);
            MissionItem cloneMissionItem = missionItem.clone();
            cloneMissionItem = objectCrudSvc.update(cloneMissionItem);
            newUid.add(cloneMissionItem.getKeyId().getObjId());
        }

        clonedMission.setMissionItemsUids(newUid);
        clonedMission = objectCrudSvc.update(clonedMission);

        return clonedMission;
    }

    @Override
    public Mission createMission() throws ObjectInstanceException {
        return objectCrudSvc.create(Mission.class.getCanonicalName());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends MissionItem> T createMissionItem(String clz) throws ObjectInstanceException {
        return (T) objectCrudSvc.create(clz);
    }
}
