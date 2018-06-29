/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.gui.persistence.services.internal;

import com.db.gui.persistence.scheme.Layer;
import com.db.gui.persistence.services.LayerCrudSvc;
import com.db.persistence.exception.DatabaseValidationException;
import com.db.persistence.exception.ObjectInstanceException;
import com.db.persistence.scheme.BaseObject;
import com.db.persistence.services.ObjectCrudSvc;
import javassist.tools.rmi.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taljmars on 3/24/17.
 */
@Component
public class LayerCrudSvcImpl implements LayerCrudSvc {

    @Autowired
    ObjectCrudSvc objectCrudSvc;

    /**
     * Will clone the mission object and every mission item.
     * Mind the the objid of each object and subobject is being regenerated
     * @param layer
     * @return
     * @throws DatabaseValidationException
     */
    @Override
    public Layer cloneLayer(Layer layer) throws DatabaseValidationException, ObjectNotFoundException, ObjectInstanceException {
        List<String> newUid = new ArrayList<>();
        Layer clonedLayer = layer.clone();
        for (String uid : clonedLayer.getObjectsUids()) {
            BaseObject item = objectCrudSvc.readByClass(uid, BaseObject.class);
            BaseObject cloneItem = item.clone();
            cloneItem = objectCrudSvc.update(cloneItem);
            newUid.add(cloneItem.getKeyId().getObjId());
        }

        clonedLayer.setObjectsUids(newUid);
        clonedLayer = objectCrudSvc.update(clonedLayer);

        return clonedLayer;
    }

    @Override
    public Layer createLayer() throws ObjectInstanceException {
        return objectCrudSvc.create(Layer.class.getCanonicalName());
    }
}
