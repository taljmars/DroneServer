package com.dronedb.persistence.services;

import com.db.persistence.exception.DatabaseValidationException;
import com.db.persistence.exception.ObjectInstanceException;
import com.dronedb.persistence.scheme.Perimeter;
import com.dronedb.persistence.scheme.Point;
import javassist.tools.rmi.ObjectNotFoundException;

/**
 * Created by taljmars on 3/27/17.
 */
public interface PerimeterCrudSvc {

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
    <T extends Perimeter> T clonePerimeter(T perimeter) throws DatabaseValidationException, ObjectNotFoundException, ObjectInstanceException;

    Point createPoint() throws ObjectInstanceException;

}
