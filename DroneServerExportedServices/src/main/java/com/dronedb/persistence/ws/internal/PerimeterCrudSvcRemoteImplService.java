
package com.dronedb.persistence.ws.internal;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "PerimeterCrudSvcRemoteImplService", targetNamespace = "http://internal.ws.persistence.dronedb.com/", wsdlLocation = "http://178.62.1.156:1234/ws/PerimeterCrudSvcRemote?wsdl")
public class PerimeterCrudSvcRemoteImplService
    extends Service
{

    private final static URL PERIMETERCRUDSVCREMOTEIMPLSERVICE_WSDL_LOCATION;
    private final static WebServiceException PERIMETERCRUDSVCREMOTEIMPLSERVICE_EXCEPTION;
    private final static QName PERIMETERCRUDSVCREMOTEIMPLSERVICE_QNAME = new QName("http://internal.ws.persistence.dronedb.com/", "PerimeterCrudSvcRemoteImplService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://178.62.1.156:1234/ws/PerimeterCrudSvcRemote?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        PERIMETERCRUDSVCREMOTEIMPLSERVICE_WSDL_LOCATION = url;
        PERIMETERCRUDSVCREMOTEIMPLSERVICE_EXCEPTION = e;
    }

    public PerimeterCrudSvcRemoteImplService() {
        super(__getWsdlLocation(), PERIMETERCRUDSVCREMOTEIMPLSERVICE_QNAME);
    }

    public PerimeterCrudSvcRemoteImplService(WebServiceFeature... features) {
        super(__getWsdlLocation(), PERIMETERCRUDSVCREMOTEIMPLSERVICE_QNAME, features);
    }

    public PerimeterCrudSvcRemoteImplService(URL wsdlLocation) {
        super(wsdlLocation, PERIMETERCRUDSVCREMOTEIMPLSERVICE_QNAME);
    }

    public PerimeterCrudSvcRemoteImplService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, PERIMETERCRUDSVCREMOTEIMPLSERVICE_QNAME, features);
    }

    public PerimeterCrudSvcRemoteImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public PerimeterCrudSvcRemoteImplService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns PerimeterCrudSvcRemote
     */
    @WebEndpoint(name = "PerimeterCrudSvcRemoteImplPort")
    public PerimeterCrudSvcRemote getPerimeterCrudSvcRemoteImplPort() {
        return super.getPort(new QName("http://internal.ws.persistence.dronedb.com/", "PerimeterCrudSvcRemoteImplPort"), PerimeterCrudSvcRemote.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns PerimeterCrudSvcRemote
     */
    @WebEndpoint(name = "PerimeterCrudSvcRemoteImplPort")
    public PerimeterCrudSvcRemote getPerimeterCrudSvcRemoteImplPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://internal.ws.persistence.dronedb.com/", "PerimeterCrudSvcRemoteImplPort"), PerimeterCrudSvcRemote.class, features);
    }

    private static URL __getWsdlLocation() {
        if (PERIMETERCRUDSVCREMOTEIMPLSERVICE_EXCEPTION!= null) {
            throw PERIMETERCRUDSVCREMOTEIMPLSERVICE_EXCEPTION;
        }
        return PERIMETERCRUDSVCREMOTEIMPLSERVICE_WSDL_LOCATION;
    }

}
