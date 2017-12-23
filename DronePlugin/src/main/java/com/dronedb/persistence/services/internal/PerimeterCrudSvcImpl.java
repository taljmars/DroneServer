package com.dronedb.persistence.services.internal;

import com.db.persistence.exception.DatabaseValidationException;
import com.db.persistence.services.ObjectCrudSvc;
import com.db.persistence.exception.ObjectInstanceException;
import com.dronedb.persistence.scheme.CirclePerimeter;
import com.dronedb.persistence.scheme.Perimeter;
import com.dronedb.persistence.scheme.Point;
import com.dronedb.persistence.scheme.PolygonPerimeter;
import com.dronedb.persistence.services.PerimeterCrudSvc;
import javassist.tools.rmi.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by taljmars on 3/27/17.
 */
@Component
public class PerimeterCrudSvcImpl implements PerimeterCrudSvc {

    @Autowired
    private ObjectCrudSvc objectCrudSvc;

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Perimeter> T clonePerimeter(T perimeter) throws DatabaseValidationException, ObjectNotFoundException, ObjectInstanceException {
        if (perimeter instanceof PolygonPerimeter)
            return (T) clonePolygon((PolygonPerimeter) perimeter);

        if (perimeter instanceof CirclePerimeter)
            return (T) cloneCircle((CirclePerimeter) perimeter);

        throw new RuntimeException("Unexpected type");
    }

    @Override
    public Point createPoint() throws ObjectInstanceException {
        return objectCrudSvc.create(Point.class.getCanonicalName());
    }

    private Perimeter cloneCircle(CirclePerimeter perimeter) throws DatabaseValidationException, ObjectNotFoundException, ObjectInstanceException {
        CirclePerimeter clonePolygon = perimeter.clone();
        Point point = objectCrudSvc.readByClass(perimeter.getCenter(), Point.class);
        Point clonePoint = point.clone();
        clonePoint = objectCrudSvc.update(clonePoint);
        clonePolygon.setCenter(point.getKeyId().getObjId());
        clonePolygon = objectCrudSvc.update(clonePolygon);
        return clonePolygon;
    }

    private PolygonPerimeter clonePolygon(PolygonPerimeter perimeter) throws DatabaseValidationException, ObjectNotFoundException, ObjectInstanceException {
        PolygonPerimeter clonePolygon = perimeter.clone();
        List<String> uuidList = new ArrayList<>();
        for (String uuid : clonePolygon.getPoints()) {
            Point point = objectCrudSvc.readByClass(uuid, Point.class);
            Point clonePoint = point.clone();
            clonePoint = objectCrudSvc.update(clonePoint);
            uuidList.add(clonePoint.getKeyId().getObjId());
        }

        clonePolygon.setPoints(uuidList);
        clonePolygon = objectCrudSvc.update(clonePolygon);

        return clonePolygon;
    }
}
