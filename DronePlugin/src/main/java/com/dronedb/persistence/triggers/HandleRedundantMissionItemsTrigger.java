package com.dronedb.persistence.triggers;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.triggers.UpdateObjectTriggerImpl;
import com.db.persistence.triggers.UpdateTrigger;
import com.dronedb.persistence.scheme.Mission;
import com.dronedb.persistence.scheme.MissionItem;
import com.db.persistence.services.ObjectCrudSvc;
import org.apache.log4j.Logger;

import java.util.UUID;

/**
 * Created by taljmars on 3/23/17.
 */
public class HandleRedundantMissionItemsTrigger extends UpdateObjectTriggerImpl {

    private final static Logger logger = Logger.getLogger(HandleRedundantMissionItemsTrigger.class);

    public HandleRedundantMissionItemsTrigger() {
        super();
    }

    @Override
    public <T extends BaseObject> void handleUpdateObject(T oldInst, T newInst, UpdateTrigger.PHASE phase) throws Exception{
        if ((!(oldInst instanceof Mission)) || (!(newInst instanceof Mission))) {
            logger.debug("Not a mission, trigger skipped");
            return;
        }

        ObjectCrudSvc objectCrudSvc = applicationContext.getBean(ObjectCrudSvc.class);

        for (UUID missionItemuid : ((Mission) oldInst).getMissionItemsUids()) {
            if (((Mission) newInst).getMissionItemsUids().contains(missionItemuid))
                continue;

            // Old Uuid, mission item should be cleared
            MissionItem missionItem = objectCrudSvc.readByClass(missionItemuid, MissionItem.class);
            objectCrudSvc.delete(missionItem);
        }
    }
}
