package com.dronedb.persistence.triggers;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.services.ObjectCrudSvc;
import com.db.persistence.triggers.DeleteObjectTriggerImpl;
import com.dronedb.persistence.scheme.CirclePerimeter;
import com.dronedb.persistence.scheme.Perimeter;
import com.dronedb.persistence.scheme.Point;
import com.dronedb.persistence.scheme.PolygonPerimeter;
import org.apache.log4j.Logger;

import java.util.UUID;

    /**
     * Created by taljmars on 3/23/17.
     */
    public class HandlePerimeterDeletionTrigger extends DeleteObjectTriggerImpl {

        private final static Logger logger = Logger.getLogger(HandlePerimeterDeletionTrigger.class);

        private ObjectCrudSvc objectCrudSvc;

        public HandlePerimeterDeletionTrigger() {
            super();
        }

        @Override
        public <T extends BaseObject> void handleDeleteObject(T inst) throws Exception {
            if (!(inst instanceof Perimeter)){
                logger.debug("Not a perimeter, trigger skipped");
                return;
            }

            objectCrudSvc = applicationContext.getBean(ObjectCrudSvc.class);

            if (inst instanceof PolygonPerimeter) {
                logger.debug("Handle polyline perimeter deletion");
                for (UUID pointUuid : ((PolygonPerimeter) inst).getPoints()) {
                    removePoint(pointUuid);
                }
                return;
            }

            if (inst instanceof CirclePerimeter) {
                logger.debug("Handle circle perimeter deletion");
                removePoint(((CirclePerimeter) inst).getCenter());
                return;
            }

            logger.error("Unexpected perimeter type");
        }

        private void removePoint(UUID pointUuid) throws Exception {
            Point point = objectCrudSvc.readByClass(pointUuid, Point.class);
            if (point == null) {
                logger.debug(String.format("Point %s wasn't found in the DB, skip it deletion", pointUuid));
                return;
            }

            objectCrudSvc.delete(point);
        }
    }
