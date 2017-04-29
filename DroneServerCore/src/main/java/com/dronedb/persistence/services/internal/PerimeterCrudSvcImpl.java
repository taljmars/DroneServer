package com.dronedb.persistence.services.internal;

import com.dronedb.persistence.exception.DatabaseValidationException;
import com.dronedb.persistence.scheme.CirclePerimeter;
import com.dronedb.persistence.scheme.Perimeter;
import com.dronedb.persistence.scheme.Point;
import com.dronedb.persistence.scheme.PolygonPerimeter;
import com.dronedb.persistence.services.DroneDbCrudSvc;
import com.dronedb.persistence.services.PerimeterCrudSvc;
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
    private DroneDbCrudSvc droneDbCrudSvc;

    @Override
    public <T extends Perimeter> T clonePerimeter(T perimeter) throws DatabaseValidationException {
        if (perimeter instanceof PolygonPerimeter)
            return (T) clonePolygon((PolygonPerimeter) perimeter);

        if (perimeter instanceof CirclePerimeter)
            return (T) cloneCircle((CirclePerimeter) perimeter);

        throw new RuntimeException("Unexpected type");
    }

    private Perimeter cloneCircle(CirclePerimeter perimeter) throws DatabaseValidationException {
        CirclePerimeter clonePolygon = perimeter.clone();
        Point point = droneDbCrudSvc.readByClass(perimeter.getCenter(), Point.class);
        Point clonePoint = point.clone();
        droneDbCrudSvc.update(clonePoint);
        clonePolygon.setCenter(point.getKeyId().getObjId());
        droneDbCrudSvc.update(clonePolygon);
        return clonePolygon;
    }

    private PolygonPerimeter clonePolygon(PolygonPerimeter perimeter) throws DatabaseValidationException {
        PolygonPerimeter clonePolygon = perimeter.clone();
        List<UUID> uuidList = new ArrayList<>();
        for (UUID uuid : clonePolygon.getPoints()) {
            Point point = droneDbCrudSvc.readByClass(uuid, Point.class);
            Point clonePoint = point.clone();
            droneDbCrudSvc.update(clonePoint);
            uuidList.add(clonePoint.getKeyId().getObjId());
        }

        clonePolygon.setPoints(uuidList);
        droneDbCrudSvc.update(clonePolygon);

        return clonePolygon;
    }
}
