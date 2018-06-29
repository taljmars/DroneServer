/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.gui.persistence.services;

import com.db.gui.persistence.scheme.Layer;
import com.db.persistence.exception.DatabaseValidationException;
import com.db.persistence.exception.ObjectInstanceException;
import javassist.tools.rmi.ObjectNotFoundException;

/**
 * Created by taljmars on 3/27/17.
 */
public interface LayerCrudSvc {

    /**
     * The following function responsible of cloning any perimeter exist in the database.
     * The cloned perimeter will have the same characteristics but with a different UUID.
     *
     * @param perimeter Required parameter to be cloned
     * @return The cloned parameter
     * @throws DatabaseValidationException in case of any issue with the fields in it
     * @throws ObjectNotFoundException in case the object or subobject of it haven't been found
     * @throws ObjectInstanceException in case on any issue occur during the new clone creation
     */
    <T extends Layer> T cloneLayer(T perimeter) throws DatabaseValidationException, ObjectNotFoundException, ObjectInstanceException;

    Layer createLayer() throws ObjectInstanceException;
}
