package com.dronedb.persistence.ws.internal;

import com.dronedb.persistence.exception.DatabaseValidationException;
import com.dronedb.persistence.exception.ObjectInstanceException;
import com.dronedb.persistence.scheme.*;
import com.dronedb.persistence.services.PerimeterCrudSvc;
import javassist.tools.rmi.ObjectNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

/**
 * Created by taljmars on 3/27/17.
 */
@Component
@WebService(endpointInterface = "com.dronedb.persistence.scheme.PerimeterCrudSvcRemote")
public class PerimeterCrudSvcRemoteImpl implements PerimeterCrudSvcRemote {

    final static Logger logger = Logger.getLogger(PerimeterCrudSvcRemoteImpl.class);

    @Autowired
    private PerimeterCrudSvc perimeterCrudSvc;

    @Override
    public <T extends Perimeter> T clonePerimeter(T perimeter) throws DatabaseValidationRemoteException, ObjectNotFoundRemoteException, ObjectInstanceRemoteException {
        try {
            logger.debug("cloning perimeter");
            return (T) perimeterCrudSvc.clonePerimeter(perimeter).copy();
        }
        catch (DatabaseValidationException e) {
            logger.error("Failed to clone perimeter", e);
            throw new DatabaseValidationRemoteException(e.getMessage());
        }
        catch (ObjectNotFoundException e) {
            logger.error("Failed to clone perimeter", e);
            throw new ObjectNotFoundRemoteException(e.getMessage());
        }
        catch (ObjectInstanceException e) {
            logger.error("Failed to clone perimeter", e);
            throw new ObjectInstanceRemoteException(e.getMessage());
        }
    }
}
