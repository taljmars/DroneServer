package com.dronedb.persistence.ws;

import com.dronedb.persistence.scheme.Mission;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import java.util.Set;

/**
 * Created by taljmars on 3/18/17.
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface MissionFacadeRemote {

    @WebMethod
    Mission write(@WebParam Mission mission);

    @WebMethod
    void delete(@WebParam Mission mission);
}
