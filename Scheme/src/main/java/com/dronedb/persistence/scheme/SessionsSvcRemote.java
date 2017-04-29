package com.dronedb.persistence.scheme;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Created by oem on 4/28/17.
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface SessionsSvcRemote {

    @WebMethod
    void publish();

    @WebMethod
    void discard();

}