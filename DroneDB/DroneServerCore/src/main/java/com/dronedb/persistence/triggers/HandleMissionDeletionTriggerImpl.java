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
public class HandleRedundantMissionItemsTriggerImpl extends UpdateObjectTriggerImpl {

    public HandleRedundantMissionItemsTriggerImpl() {
        super();
    }

    @Override
    public <T extends BaseObject> void handleUpdateObject(T oldInst, T newInst, UpdateTrigger.PHASE phase) {
        if ((!(oldInst instanceof Mission)) || (!(newInst instanceof Mission))) {
            System.out.println("No a mission, trigger skipped");
            return;
        }

        QuerySvc querySvc = applicationContext.getBean(QuerySvc.class);
        DroneDbCrudSvc droneDbCrudSvc = applicationContext.getBean(DroneDbCrudSvc.class);

        for (UUID missionItemuid : ((Mission) oldInst).getMissionItemsUids()) {
            if (((Mission) newInst).getMissionItemsUids().contains(missionItemuid))
                continue;

            // Old Uuid, mission item should be cleared
            MissionItem missionItem = droneDbCrudSvc.readByClass(missionItemuid, MissionItem.class);
            droneDbCrudSvc.delete(missionItem);
        }
    }
}
