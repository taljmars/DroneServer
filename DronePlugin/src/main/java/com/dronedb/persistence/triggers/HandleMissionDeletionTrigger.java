package com.dronedb.persistence.triggers;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.triggers.DeleteObjectTriggerImpl;
import com.dronedb.persistence.scheme.Mission;
import com.dronedb.persistence.scheme.MissionItem;
import com.db.persistence.services.ObjectCrudSvc;
import org.apache.log4j.Logger;

/**
 * Created by taljmars on 3/23/17.
 */
public class HandleMissionDeletionTrigger extends DeleteObjectTriggerImpl {

    private final static Logger LOGGER = Logger.getLogger(HandleMissionDeletionTrigger.class);

    public HandleMissionDeletionTrigger() {
        super();
    }

    @Override
    public <T extends BaseObject> void handleDeleteObject(T inst) throws Exception {
        if (!(inst instanceof Mission)){
            LOGGER.debug("Not a mission, trigger skipped");
            return;
        }

        ObjectCrudSvc objectCrudSvc = applicationContext.getBean(ObjectCrudSvc.class);

        for (String missionItemuid : ((Mission) inst).getMissionItemsUids()) {
            MissionItem missionItem = objectCrudSvc.readByClass(missionItemuid, MissionItem.class);
            if (missionItem == null) {
                LOGGER.debug(String.format("Mission Item %s wasn't found in the DB, skip it deletion", missionItemuid));
                continue;
            }

            objectCrudSvc.delete(missionItem);
        }
    }
}
