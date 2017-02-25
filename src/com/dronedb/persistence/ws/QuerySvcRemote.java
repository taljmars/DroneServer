package com.dronedb.ws;

import java.util.Collection;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;

import com.dronedb.scheme.BaseObject;
import com.sun.xml.internal.ws.developer.Serialization;

@WebService
@SOAPBinding(style = Style.RPC)
public interface QuerySvcRemote {
	
	@WebMethod
	<T extends BaseObject> QueryResponseRemote runNativeQuery(@WebParam String queryString,@WebParam Class<T> clz);
	
	@WebMethod
	<T extends BaseObject> QueryResponseRemote runNamedQuery(@WebParam String queryString,@WebParam Class<T> clz);

}
