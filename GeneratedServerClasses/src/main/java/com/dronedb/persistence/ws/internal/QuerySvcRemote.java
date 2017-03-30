
package com.dronedb.persistence.ws.internal;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import com.dronedb.persistence.scheme.apis.ObjectFactory;
import com.dronedb.persistence.scheme.apis.QueryRequestRemote;
import com.dronedb.persistence.scheme.apis.QueryResponseRemote;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "QuerySvcRemote", targetNamespace = "http://apis.scheme.persistence.dronedb.com/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface QuerySvcRemote {


    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns com.dronedb.persistence.scheme.apis.QueryResponseRemote
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://apis.scheme.persistence.dronedb.com/QuerySvcRemote/runNativeQueryRequest", output = "http://apis.scheme.persistence.dronedb.com/QuerySvcRemote/runNativeQueryResponse")
    public QueryResponseRemote runNativeQuery(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0,
        @WebParam(name = "arg1", partName = "arg1")
        String arg1);

    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns com.dronedb.persistence.scheme.apis.QueryResponseRemote
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://apis.scheme.persistence.dronedb.com/QuerySvcRemote/runNamedQueryRequest", output = "http://apis.scheme.persistence.dronedb.com/QuerySvcRemote/runNamedQueryResponse")
    public QueryResponseRemote runNamedQuery(
        @WebParam(name = "arg0", partName = "arg0")
        String arg0,
        @WebParam(name = "arg1", partName = "arg1")
        String arg1);

    /**
     * 
     * @param arg0
     * @return
     *     returns com.dronedb.persistence.scheme.apis.QueryResponseRemote
     */
    @WebMethod
    @WebResult(partName = "return")
    @Action(input = "http://apis.scheme.persistence.dronedb.com/QuerySvcRemote/queryRequest", output = "http://apis.scheme.persistence.dronedb.com/QuerySvcRemote/queryResponse")
    public QueryResponseRemote query(
        @WebParam(name = "arg0", partName = "arg0")
        QueryRequestRemote arg0);

}
