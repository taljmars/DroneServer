package com.dronedb.persistence.triggers;

import com.dronedb.persistence.scheme.BaseObject;
import com.dronedb.persistence.scheme.mission.Mission;
import com.dronedb.persistence.scheme.mission.MissionItem;
import com.dronedb.persistence.services.DroneDbCrudSvc;
import com.dronedb.persistence.services.QuerySvc;
import com.dronedb.triggers.UpdateTrigger;

import java.util.UUID;

/**
 * Created by taljmars on 3/23/17.
 */
public class HandleMissionDeletionTriggerImpl extends DeleteObjectTriggerImpl {

    public HandleMissionDeletionTriggerImpl() {
        super();
    }

    @Override
    public <T extends BaseObject> void handleDeleteObject(T inst) {
        if (!(inst instanceof Mission)){
            System.out.println("No a mission, trigger skipped");
            return;
        }

        DroneDbCrudSvc droneDbCrudSvc = applicationContext.getBean(DroneDbCrudSvc.class);

        for (UUID missionItemuid : ((Mission) inst).getMissionItemsUids()) {
            MissionItem missionItem = droneDbCrudSvc.readByClass(missionItemuid, MissionItem.class);
            droneDbCrudSvc.delete(missionItem);
        }
    }
}
