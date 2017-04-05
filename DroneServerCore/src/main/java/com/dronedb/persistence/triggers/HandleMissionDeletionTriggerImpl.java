package com.dronedb.persistence.triggers;

import com.dronedb.persistence.scheme.BaseObject;
import com.dronedb.persistence.scheme.Mission;
import com.dronedb.persistence.scheme.MissionItem;
import com.dronedb.persistence.services.DroneDbCrudSvc;

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
            if (missionItem == null) {
                System.out.println("Mission Item " + missionItemuid + " wasn't found in the DB, skip it deletion");
                continue;
            }
            droneDbCrudSvc.delete(missionItem);
        }
    }
}
