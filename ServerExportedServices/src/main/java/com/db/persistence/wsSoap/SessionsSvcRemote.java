package com.db.persistence.wsSoap;

import javax.jws.WebMethod;
import javax.jws.WebService;

import static com.db.persistence.wsSoap.Constants.WS_NAMESPACE;

/**
 * Created by taljmars on 4/28/17.
 */
@WebService(targetNamespace = WS_NAMESPACE + "/SessionsSvcRemote")
//@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface SessionsSvcRemote {

    @WebMethod
    void publish();

    @WebMethod
    void discard();

}