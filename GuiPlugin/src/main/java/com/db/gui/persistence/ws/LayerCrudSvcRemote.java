/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.gui.persistence.ws;

import com.db.gui.persistence.scheme.Layer;
import com.db.persistence.remote_exception.DatabaseValidationRemoteException;
import com.db.persistence.remote_exception.ObjectInstanceRemoteException;
import com.db.persistence.remote_exception.ObjectNotFoundRemoteException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by taljmars on 3/24/17.
 */
public interface LayerCrudSvcRemote {

    @RequestMapping(value = "/cloneLayer", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<Layer> cloneLayer(@RequestBody Layer layer) throws DatabaseValidationRemoteException, ObjectNotFoundRemoteException, ObjectInstanceRemoteException;

    @RequestMapping(value = "/createLayer", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<Layer> createLayer() throws ObjectInstanceRemoteException;

}
