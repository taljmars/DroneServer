package com.dronedb.persistence.triggers;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.services.ObjectCrudSvc;
import com.db.persistence.triggers.UpdateObjectTrigger;
import com.db.persistence.triggers.UpdateObjectTriggerImpl;
import com.db.persistence.triggers.UpdateTrigger;
import com.dronedb.persistence.scheme.CirclePerimeter;
import com.dronedb.persistence.scheme.Perimeter;
import com.dronedb.persistence.scheme.Point;
import com.dronedb.persistence.scheme.PolygonPerimeter;
import org.apache.log4j.Logger;

import java.util.UUID;

/**
 * Created by taljmars on 3/23/17.
 */
public class HandleRedundantPointsTrigger extends UpdateObjectTriggerImpl {

    private final static Logger LOGGER = Logger.getLogger(HandleRedundantPointsTrigger.class);

    public HandleRedundantPointsTrigger() {
        super();
    }


    @Override
    public <T extends BaseObject> void handleUpdateObject(T oldInst, T newInst, UpdateTrigger.PHASE phase) throws Exception{
        if ((!(oldInst instanceof Perimeter)) || (!(newInst instanceof Perimeter))) {
            LOGGER.debug("Not a perimeter, trigger skipped");
            return;
        }

        if (!oldInst.getClass().equals(newInst.getClass())) {
            LOGGER.error("Perimeter are not of the same, old instance " + oldInst.getClass() + " new instance " + newInst.getClass());
            return;
        }

        ObjectCrudSvc objectCrudSvc = applicationContext.getBean(ObjectCrudSvc.class);
        LOGGER.debug("Old: " + oldInst);
        LOGGER.debug("New: " + newInst);
        LOGGER.debug("Phase: " + phase);

        if (oldInst instanceof PolygonPerimeter) {
            LOGGER.debug("Handle polyline perimeter update");
            for (UUID pointUuid : ((PolygonPerimeter) oldInst).getPoints()) {
                if (((PolygonPerimeter) newInst).getPoints().contains(pointUuid))
                    continue;

                // Old Uuid, mission item should be cleared
                Point point = objectCrudSvc.readByClass(pointUuid, Point.class);
                objectCrudSvc.delete(point);
            }
            return;
        }

        if (oldInst instanceof CirclePerimeter) {
            LOGGER.debug("Handle circle perimeter update");
            if (((CirclePerimeter) oldInst).getCenter() == null)
                return;
            if (!((CirclePerimeter) oldInst).getCenter().equals(((CirclePerimeter) newInst).getCenter())) {
                LOGGER.debug("Center was changed, remove item");
                Point point = objectCrudSvc.readByClass(((CirclePerimeter) oldInst).getCenter(), Point.class);
                objectCrudSvc.delete(point);
            }
            return;
        }


    }
}
