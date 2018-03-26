/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.dronedb.persistence.ws;

import com.db.persistence.remote_exception.DatabaseValidationRemoteException;
import com.db.persistence.remote_exception.ObjectInstanceRemoteException;
import com.db.persistence.remote_exception.ObjectNotFoundRemoteException;
import com.dronedb.persistence.scheme.Perimeter;
import com.dronedb.persistence.scheme.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by taljmars on 3/27/17.
 */
public interface PerimeterCrudSvcRemote {

    @RequestMapping(value = "/clonePerimeter", method = RequestMethod.POST)
    @ResponseBody
    <T extends Perimeter> ResponseEntity<T> clonePerimeter(@RequestBody T perimeter) throws DatabaseValidationRemoteException, ObjectNotFoundRemoteException, ObjectInstanceRemoteException;

    @RequestMapping(value = "/createPoint", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<Point> createPoint() throws ObjectInstanceRemoteException;
}
