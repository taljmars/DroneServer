package com.dronedb.persistence.scheme;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Created by oem on 3/27/17.
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface PerimeterCrudSvcRemote {

    <T extends Perimeter> T clonePerimeter(@WebParam T perimeter) throws DatabaseRemoteValidationException;

}
