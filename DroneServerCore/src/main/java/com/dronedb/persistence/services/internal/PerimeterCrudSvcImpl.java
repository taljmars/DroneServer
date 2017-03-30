package com.dronedb.persistence.services.internal;

import com.dronedb.persistence.scheme.perimeter.CirclePerimeter;
import com.dronedb.persistence.scheme.perimeter.Perimeter;
import com.dronedb.persistence.scheme.perimeter.Point;
import com.dronedb.persistence.scheme.perimeter.PolygonPerimeter;
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
    public <T extends Perimeter> T clonePerimeter(T perimeter) {
        if (perimeter instanceof PolygonPerimeter)
            return (T) clonePolygon((PolygonPerimeter) perimeter);

        if (perimeter instanceof CirclePerimeter)
            return (T) cloneCircle((CirclePerimeter) perimeter);

        throw new RuntimeException("Unexpected type");
    }

    private Perimeter cloneCircle(CirclePerimeter perimeter) {
        CirclePerimeter clonePolygon = perimeter.clone();
        Point point = droneDbCrudSvc.readByClass(perimeter.getCenter(), Point.class);
        Point clonePoint = point.clone();
        droneDbCrudSvc.update(clonePoint);
        clonePolygon.setCenter(point.getObjId());
        droneDbCrudSvc.update(clonePolygon);
        return clonePolygon;
    }

    private PolygonPerimeter clonePolygon(PolygonPerimeter perimeter) {
        PolygonPerimeter clonePolygon = perimeter.clone();
        List<UUID> uuidList = new ArrayList<>();
        for (UUID uuid : clonePolygon.getPoints()) {
            Point point = droneDbCrudSvc.readByClass(uuid, Point.class);
            Point clonePoint = point.clone();
            droneDbCrudSvc.update(clonePoint);
            uuidList.add(clonePoint.getObjId());
        }

        clonePolygon.setPoints(uuidList);
        droneDbCrudSvc.update(clonePolygon);

        return clonePolygon;
    }
}
