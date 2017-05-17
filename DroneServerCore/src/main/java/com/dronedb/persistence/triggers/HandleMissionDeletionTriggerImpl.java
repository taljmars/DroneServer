package com.dronedb.persistence.triggers;

import com.dronedb.persistence.scheme.BaseObject;
import com.dronedb.persistence.scheme.Mission;
import com.dronedb.persistence.scheme.MissionItem;
import com.dronedb.persistence.services.DroneDbCrudSvc;
import com.dronedb.persistence.services.internal.DroneDbCrudSvcImpl;
import org.apache.log4j.Logger;

import java.util.UUID;

/**
 * Created by taljmars on 3/23/17.
 */
public class HandleMissionDeletionTriggerImpl extends DeleteObjectTriggerImpl {

    private final static Logger logger = Logger.getLogger(HandleMissionDeletionTriggerImpl.class);

    public HandleMissionDeletionTriggerImpl() {
        super();
    }

    @Override
    public <T extends BaseObject> void handleDeleteObject(T inst) throws Exception {
        if (!(inst instanceof Mission)){
            logger.debug("No a mission, trigger skipped");
            return;
        }

        DroneDbCrudSvc droneDbCrudSvc = applicationContext.getBean(DroneDbCrudSvc.class);

        for (UUID missionItemuid : ((Mission) inst).getMissionItemsUids()) {
            MissionItem missionItem = droneDbCrudSvc.readByClass(missionItemuid, MissionItem.class);
            if (missionItem == null) {
                logger.debug(String.format("Mission Item %s wasn't found in the DB, skip it deletion", missionItemuid));
                continue;
            }

            droneDbCrudSvc.delete(missionItem);
        }
    }
}
