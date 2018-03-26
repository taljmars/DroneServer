/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.dronedb.persistence.ws.internal;

import com.db.persistence.events.ServerEventMapper;
import com.db.persistence.exception.DatabaseValidationException;
import com.db.persistence.exception.ObjectInstanceException;
import com.db.persistence.remote_exception.DatabaseValidationRemoteException;
import com.db.persistence.remote_exception.ObjectInstanceRemoteException;
import com.db.persistence.remote_exception.ObjectNotFoundRemoteException;
import com.dronedb.persistence.scheme.Perimeter;
import com.dronedb.persistence.scheme.Point;
import com.dronedb.persistence.services.PerimeterCrudSvc;
import com.dronedb.persistence.ws.PerimeterCrudSvcRemote;
import javassist.tools.rmi.ObjectNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

/**
 * Created by taljmars on 3/27/17.
 */
@RestController
public class PerimeterCrudSvcRemoteImpl implements PerimeterCrudSvcRemote {

    private final static Logger LOGGER = Logger.getLogger(PerimeterCrudSvcRemoteImpl.class);

    @Autowired
    private PerimeterCrudSvc perimeterCrudSvc;

    @Autowired
    ServerEventMapper serverEventMapper;

    @PostConstruct
    public void init() {
        LOGGER.debug("Initialize Perimeter CRUD");
    }

    @Override
    @RequestMapping(value = "/clonePerimeter", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public <T extends Perimeter> ResponseEntity<T> clonePerimeter(@RequestBody T perimeter)
            throws DatabaseValidationRemoteException, ObjectNotFoundRemoteException, ObjectInstanceRemoteException {

        try {
            LOGGER.debug("cloning perimeter");
            T clonedPerimeter = (T) perimeterCrudSvc.clonePerimeter(perimeter).copy();
            return new ResponseEntity<T>(clonedPerimeter, HttpStatus.OK);
        }
        catch (DatabaseValidationException e) {
            LOGGER.error("Failed to clone perimeter", e);
            throw new DatabaseValidationRemoteException(e.getMessage());
        }
        catch (ObjectNotFoundException e) {
            LOGGER.error("Failed to clone perimeter", e);
            throw new ObjectNotFoundRemoteException(e.getMessage());
        }
        catch (ObjectInstanceException e) {
            LOGGER.error("Failed to clone perimeter", e);
            throw new ObjectInstanceRemoteException(e.getMessage());
        }
    }

    @Override
    @RequestMapping(value = "/createPoint", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Point> createPoint() throws ObjectInstanceRemoteException {
        try {
            Point point = (Point) perimeterCrudSvc.createPoint().copy();
            return new ResponseEntity<Point>(point, HttpStatus.OK);
        } catch (ObjectInstanceException e) {
            LOGGER.error("Failed to create point", e);
            throw new ObjectInstanceRemoteException(e.getMessage());
        }
    }
}
