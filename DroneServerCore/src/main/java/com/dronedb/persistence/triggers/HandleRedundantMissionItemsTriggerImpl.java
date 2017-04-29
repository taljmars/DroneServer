package com.dronedb.persistence.triggers;

import com.dronedb.persistence.exception.DatabaseValidationException;
import com.dronedb.persistence.scheme.BaseObject;
import com.dronedb.persistence.scheme.Mission;
import com.dronedb.persistence.scheme.MissionItem;
import com.dronedb.persistence.services.DroneDbCrudSvc;
import com.dronedb.persistence.services.QuerySvc;
import com.dronedb.persistence.triggers.UpdateTrigger;

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
            try {
                droneDbCrudSvc.delete(missionItem);
            } catch (DatabaseValidationException e) {
                e.printStackTrace();
            }
        }
    }
}
