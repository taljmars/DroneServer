package com.dronedb.persistence.services;

import com.dronedb.persistence.scheme.perimeter.Perimeter;

/**
 * Created by oem on 3/27/17.
 */
public interface PerimeterCrudSvc {

    <T extends Perimeter> T clonePerimeter(T perimeter);

}
