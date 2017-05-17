package com.dronedb.persistence.triggers;

import com.dronedb.persistence.scheme.BaseObject;
import com.dronedb.persistence.scheme.Mission;
import com.dronedb.persistence.scheme.MissionItem;
import com.dronedb.persistence.services.DroneDbCrudSvc;
import org.apache.log4j.Logger;

import java.util.UUID;

/**
 * Created by taljmars on 3/23/17.
 */
public class HandleRedundantMissionItemsTriggerImpl extends UpdateObjectTriggerImpl {

    private final static Logger logger = Logger.getLogger(HandleRedundantMissionItemsTriggerImpl.class);

    public HandleRedundantMissionItemsTriggerImpl() {
        super();
    }

    @Override
    public <T extends BaseObject> void handleUpdateObject(T oldInst, T newInst, UpdateTrigger.PHASE phase) throws Exception{
        if ((!(oldInst instanceof Mission)) || (!(newInst instanceof Mission))) {
            logger.debug("No a mission, trigger skipped");
            return;
        }

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
