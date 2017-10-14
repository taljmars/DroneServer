package com.dronedb.persistence.ws;

import com.db.persistence.remote_exception.DatabaseValidationRemoteException;
import com.db.persistence.remote_exception.ObjectInstanceRemoteException;
import com.db.persistence.remote_exception.ObjectNotFoundRemoteException;
import com.dronedb.persistence.scheme.Perimeter;
import com.dronedb.persistence.scheme.Point;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Created by taljmars on 3/27/17.
 */
@WebService(targetNamespace = "http://scheme.persistence.dronedb.com/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface PerimeterCrudSvcRemote {

    <T extends Perimeter> T clonePerimeter(@WebParam T perimeter) throws DatabaseValidationRemoteException, ObjectNotFoundRemoteException, ObjectInstanceRemoteException;

    Point createPoint() throws ObjectInstanceRemoteException;
}
