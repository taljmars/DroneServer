package com.dronedb.persistence.services;

import com.dronedb.persistence.exception.DatabaseValidationException;
import com.dronedb.persistence.scheme.Perimeter;

/**
 * Created by taljmars on 3/27/17.
 */
public interface PerimeterCrudSvc {

    <T extends Perimeter> T clonePerimeter(T perimeter) throws DatabaseValidationException;

}
