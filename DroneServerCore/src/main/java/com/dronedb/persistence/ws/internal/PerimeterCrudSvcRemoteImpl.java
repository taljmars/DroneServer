package com.dronedb.persistence.ws.internal;

import com.dronedb.persistence.exception.DatabaseValidationException;
import com.dronedb.persistence.scheme.DatabaseRemoteValidationException;
import com.dronedb.persistence.scheme.PerimeterCrudSvcRemote;
import com.dronedb.persistence.scheme.Perimeter;
import com.dronedb.persistence.services.PerimeterCrudSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

/**
 * Created by oem on 3/27/17.
 */
@Component
@WebService(endpointInterface = "com.dronedb.persistence.scheme.PerimeterCrudSvcRemote")
public class PerimeterCrudSvcRemoteImpl implements PerimeterCrudSvcRemote {

    @Autowired
    private PerimeterCrudSvc perimeterCrudSvc;

    @Override
    public <T extends Perimeter> T clonePerimeter(T perimeter) throws DatabaseRemoteValidationException {
        try {
            return perimeterCrudSvc.clonePerimeter(perimeter);
        }
        catch (DatabaseValidationException e) {
            throw new DatabaseRemoteValidationException(e.getMessage());
        }
    }
}
