/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.gui.persistence.ws.internal;

import com.db.gui.persistence.scheme.Layer;
import com.db.gui.persistence.services.LayerCrudSvc;
import com.db.gui.persistence.ws.LayerCrudSvcRemote;
import com.db.persistence.exception.DatabaseValidationException;
import com.db.persistence.exception.ObjectInstanceException;
import com.db.persistence.remote_exception.DatabaseValidationRemoteException;
import com.db.persistence.remote_exception.ObjectInstanceRemoteException;
import com.db.persistence.remote_exception.ObjectNotFoundRemoteException;
import javassist.tools.rmi.ObjectNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by taljmars on 3/24/17.
 */
@RestController
public class LayerCrudSvcRemoteImpl implements LayerCrudSvcRemote
{
    private final static Logger LOGGER = Logger.getLogger(LayerCrudSvcRemoteImpl.class);

    @Autowired
    private LayerCrudSvc layerCrudSvc;

    /**
     * Will clone the layer object and every layer item.
     * Mind the the objid of each object and subobject is being regenerated
     * @param layer
     * @return
     * @throws DatabaseValidationRemoteException
     */
    @Override
    @RequestMapping(value = "/cloneLayer", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Layer> cloneLayer(@RequestBody Layer layer) throws DatabaseValidationRemoteException, ObjectNotFoundRemoteException, ObjectInstanceRemoteException {
        try {
            Layer clonedLayer = (Layer) layerCrudSvc.cloneLayer(layer).copy();
            return new ResponseEntity<Layer>(clonedLayer, HttpStatus.OK);
        }
        catch (DatabaseValidationException e) {
            LOGGER.error("Failed to clone layer", e);
            throw new DatabaseValidationRemoteException(e.getMessage());
        }
        catch (ObjectNotFoundException e) {
            LOGGER.error("Failed to clone layer", e);
            throw new ObjectNotFoundRemoteException(e.getMessage());
        }
        catch (ObjectInstanceException e) {
            LOGGER.error("Failed to clone layer", e);
            throw new ObjectInstanceRemoteException(e.getMessage());
        }
    }

    @Override
    @RequestMapping(value = "/createLayer", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Layer> createLayer() throws ObjectInstanceRemoteException {
        try {
            Layer layer = (Layer) layerCrudSvc.createLayer().copy();
            return new ResponseEntity<Layer>(layer, HttpStatus.OK);
        }
        catch (ObjectInstanceException e) {
            LOGGER.error("Failed to create layer", e);
            throw new ObjectInstanceRemoteException(e.getMessage());
        }
    }
}
