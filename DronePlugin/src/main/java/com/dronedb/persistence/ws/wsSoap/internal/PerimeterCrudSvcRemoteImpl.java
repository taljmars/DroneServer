package com.dronedb.persistence.ws.wsSoap.internal;

import com.dronedb.persistence.scheme.Perimeter;
import com.dronedb.persistence.scheme.Point;
import com.dronedb.persistence.ws.wsSoap.PerimeterCrudSvcRemote;
import com.db.persistence.exception.DatabaseValidationException;
import com.db.persistence.remote_exception.DatabaseValidationRemoteException;
import com.db.persistence.exception.ObjectInstanceException;
import com.db.persistence.remote_exception.ObjectInstanceRemoteException;
import com.dronedb.persistence.services.PerimeterCrudSvc;
import com.db.persistence.remote_exception.ObjectNotFoundRemoteException;
import javassist.tools.rmi.ObjectNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

/**
 * Created by taljmars on 3/27/17.
 */
@Component
@WebService(endpointInterface = "com.dronedb.persistence.ws.PerimeterCrudSvcRemote",
        targetNamespace = "http://scheme.persistence.dronedb.com/")
public class PerimeterCrudSvcRemoteImpl implements PerimeterCrudSvcRemote {

    final static Logger logger = Logger.getLogger(PerimeterCrudSvcRemoteImpl.class);

    @Autowired
    private PerimeterCrudSvc perimeterCrudSvc;

    @Override
    @SuppressWarnings("unchecked")
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

    @Override
    public Point createPoint() throws ObjectInstanceRemoteException {
        try {
            return perimeterCrudSvc.createPoint();
        } catch (ObjectInstanceException e) {
            logger.error("Failed to create point", e);
            throw new ObjectInstanceRemoteException(e.getMessage());
        }
    }
}
